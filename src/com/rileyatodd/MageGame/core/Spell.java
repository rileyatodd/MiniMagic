package com.rileyatodd.MageGame.core;

import com.rileyatodd.MageGame.Scenery;
import com.rileyatodd.MageGame.userInterface.ButtonCallback;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Spell extends GameObject implements ButtonCallback {

	private static final String TAG = "Spell";
	public GameObject caster;
	public Paint paint;
	public int range;
	
	public Spell(GameObject caster, GameObject destination, Bitmap bitmap, int x, int y, GameInstance gameInstance, String name) {
		super(bitmap, x, y, gameInstance, name);
		this.solid = false;
		this.caster = caster;
		this.attachObserver(caster);
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	@Override
	public void onCollision(GameObject object){
		Log.d("Spell", "onCollision called");
		if (object instanceof Character && object != caster) {
			Character character = (Character) object;
			damageTarget(character, 1);
			this.despawn();
		}
		
		if (object instanceof Scenery) {
			Log.d(TAG, "spell collided with scenery");
			this.despawn();
		}
	}
	
	public void detachSubject(Subject observed) {
		if (observed == destination) {
			this.despawn();
		}
	}
	
	public void onButtonPress() {
		gameInstance.player1.castSpell(this, 0, 0);
	}
	
	public void onCast(Character caster, int x, int y) {
		//default behavior is to initialize itself and add itself to the gameInstance
		gameInstance.toAdd.add(this);
		this.attachObserver(caster);
		this.destination.attachObserver(this);
	}
	
	public void healTarget(Character target, int amount) {
		if (amount < target.maxHealth - target.remainingHealth) {
			target.remainingHealth += amount;
		} else {
			target.remainingHealth = target.maxHealth;
		}
		target.notifyObservers();
	}
	
	public void damageTarget(Character target, int amount) {
		target.remainingHealth -= amount;
		target.notifyObservers();
	}
}
