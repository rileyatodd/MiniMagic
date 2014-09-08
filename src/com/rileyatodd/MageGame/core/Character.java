package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.graphics.BitmapFactory;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.spells.DivineGrace;
import com.rileyatodd.MageGame.spells.Flash;

public class Character extends GameObject {
	public int lvl;
	public GameObject target = null;
	public int attackSpeed = 10;
	public long lastAttackTime;
	public int maxHealth;
	public int remainingHealth;
	public int Resistance;
	public ArrayList<Spell> activeSpells;
	public ArrayList<Spell> learnedSpells;
	public ArrayList<PlayerEffect> activeEffects;
	
	
	public Character(int lvl, Drawable drawable, int x, int y, GameInstance gameInstance, String name) {
		super(drawable, x, y, gameInstance, name);
		this.lvl = lvl;
		this.maxHealth = this.lvl * 20;
		this.remainingHealth = this.maxHealth;
		lastAttackTime = 0;
		activeEffects = new ArrayList<PlayerEffect>();
		activeSpells = new ArrayList<Spell>();
		Spell spell1 = new DivineGrace(this, null, null, 0, 0, gameInstance, "DivineGrace");
		activeSpells.add(spell1);
		Spell spell2 = new Flash(this, null, null, 0, 0, gameInstance, "Flash");
		activeSpells.add(spell2);
		
	}
	
	public void die() {
		despawn();
	}
	
	public synchronized void update(){
		if (remainingHealth <= 0) {
			die();
		} else { 
			super.update();	
			//if timer is up, auto-attack
			if (target != null) {
				if (System.currentTimeMillis() - lastAttackTime > 1000/attackSpeed) {
					this.autoAttack(target);
				}
			}
			//Handle conditions
			for (PlayerEffect effect : activeEffects) {
				if (effect.endTime != 0 && System.currentTimeMillis() > effect.endTime) {
					effect.deactivate();
				}
			}
		}
	}
	
	public void autoAttack(GameObject target) {
		Spell fireball = new Spell(this, target, new BitmapDrawable(BitmapFactory.decodeResource(gameInstance.gameActivity.getResources(), R.drawable.fireball)),
				this.shape.getCenter().x, this.shape.getCenter().y, gameInstance, name + "'s AutoAttack");
		fireball.destination = this.target;
		fireball.speed = 10;
		this.castSpell(fireball, this.shape.getCenter().x, this.shape.getCenter().y);
		lastAttackTime = System.currentTimeMillis();
	}
	
	
	//get/set lvl
	public void setLvl(int x) {
		this.lvl = x;
		this.maxHealth = this.lvl * 20;
		this.remainingHealth = this.maxHealth;
	}
	public int getLevel() {
		return this.lvl;
	}
	
	//get/set target
	public void setTarget(GameObject target) {
		this.target = target;
		target.attachObserver(this);
		notifyObservers("target");
	}
	public void setTarget() {
		this.target = null;
		notifyObservers("target");
	}
	
	public GameObject getTarget() {
		return this.target;
	}
	
	public void setHealth(int x) {
		this.remainingHealth = x;
		notifyObservers("health");
	}
	
	public void castSpell(Spell spell, int x, int y) {
		spell.onCast(this, x, y);
	}
	
	public void updateSubject(Subject sub, String message) {
		if (message.equals("despawn")) {
			if (sub == target) {
				setTarget();
			}
		}
		super.updateSubject(sub, message);
	}

}
