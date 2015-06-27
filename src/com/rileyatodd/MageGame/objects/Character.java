package com.rileyatodd.MageGame.objects;

import java.util.ArrayList;

import android.util.Log;

import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.PlayerEffect;
import com.rileyatodd.MageGame.core.Subject;
import com.rileyatodd.MageGame.geometry.Point;
import com.rileyatodd.MageGame.spells.DivineGrace;
import com.rileyatodd.MageGame.spells.Fireball;
import com.rileyatodd.MageGame.spells.Flash;
import com.rileyatodd.MageGame.spells.Spell;

public class Character extends GameObject {
	private int lvl;
	private GameObject target = null;
	private Spell autoAttack;
	private float power = 1;
	private float haste = 1;
	private int maxHealth;
	public int remainingHealth;
	public int Resistance;
	public ArrayList<Spell> activeSpells;
	public ArrayList<Spell> learnedSpells;
	public ArrayList<PlayerEffect> activeEffects;
	
	
	public Character(int lvl, GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		super(gameInstance, drawable, loc, name);
		this.setLvl(lvl);
		this.setMaxHealth(this.getLvl() * 20);
		this.remainingHealth = this.getMaxHealth();
		activeEffects = new ArrayList<PlayerEffect>();
		activeSpells = new ArrayList<Spell>();
		activeSpells.add(new DivineGrace(gameInstance));
		activeSpells.add(new Flash(gameInstance));
		setAutoAttack(new Fireball(gameInstance));
	}
	
	public Character(Character other) {
		super(other.gameInstance, other.getDrawable(), other.shape.getCenter(), other.name);
		this.lvl = other.getLvl();
		this.setMaxHealth(other.getMaxHealth());
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
				if (autoAttack.getRemainingCooldown() <= 0) {
					Log.d("Character", "AutoAttaking");
					this.castSpell(getAutoAttack(), this.shape.getCenter());
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
	
	//get/set lvl
	public void setLvl(int x) {
		this.lvl = x;
		this.setMaxHealth(this.lvl * 20);
		this.remainingHealth = this.getMaxHealth();
	}
	public int getLevel() {
		return this.getLvl();
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
	
	public void castSpell(Spell spell, Point loc) {
		spell.onCast(this, new Point(loc));
	}
	
	public void damage(int amount) {
		this.setHealth(this.remainingHealth - amount);
	}
	
	public void heal(int amount) {
		int newTotal = remainingHealth + amount;
		remainingHealth = newTotal <= getMaxHealth() ? newTotal: getMaxHealth();
	}
	
	public void updateSubject(Subject sub, String message) {
		if (message.equals("despawn")) {
			if (sub == target) {
				setTarget();
			}
		}
		super.updateSubject(sub, message);
	}

	public int getLvl() {
		return lvl;
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public Spell getAutoAttack() {
		return autoAttack;
	}

	public void setAutoAttack(Spell autoAttack) {
		this.autoAttack = autoAttack;
	}

	public float getHaste() {
		return haste;
	}

	public void setHaste(float haste) {
		this.haste = haste;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

}
