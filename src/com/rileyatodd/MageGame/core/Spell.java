package com.rileyatodd.MageGame.core;

import android.graphics.Color;
import android.graphics.Paint;

import com.rileyatodd.MageGame.Scenery;
import com.rileyatodd.MageGame.userInterface.Button;
import com.rileyatodd.MageGame.userInterface.ButtonCallback;

public class Spell extends GameObject implements ButtonCallback {

	public GameObject caster;
	public Paint paint;
	public int range;
	
	public Spell(GameObject caster, GameObject destination, Drawable drawable, int x, int y, GameInstance gameInstance, String name) {
		super(drawable, x, y, gameInstance, name);
		this.solid = false;
		this.caster = caster;
		this.attachObserver(caster);
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public void onCollision(GameObject object){
		if (object instanceof Character && object != caster) {
			Character character = (Character) object;
			damageTarget(character, 1);
			this.despawn();
		}
		
		if (object instanceof Scenery) {
			this.despawn();
		}
	}
	
	public void onButtonPress(Button button) {
		onActivate(gameInstance.player1, 0, 0);
	}
	
	public void onActivate(Character caster, int x, int y) {
		onCast(caster, x, y);
	}
	
	public void onCast(Character caster, int x, int y) {
		//default behavior is to initialize itself and add itself to the gameInstance
		gameInstance.addObject(this);
		this.attachObserver(caster);
		this.destination.attachObserver(this);
	}
	
	public void healTarget(Character target, int amount) {
		if (amount < target.maxHealth - target.remainingHealth) {
			target.remainingHealth += amount;
		} else {
			target.remainingHealth = target.maxHealth;
		}
		target.notifyObservers("health");
	}
	
	public void damageTarget(Character target, int amount) {
		target.remainingHealth -= amount;
		target.notifyObservers("health");
	}
	
	public void updateSubject(Subject sub, String message) {
		if (message.equals("despawn")) {
			if (sub == destination) {
				despawn();
			}
		}
	}
}
