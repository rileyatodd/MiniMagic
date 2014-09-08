package com.rileyatodd.MageGame;

import android.graphics.BitmapFactory;

import com.rileyatodd.MageGame.core.BitmapDrawable;
import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;

public class Rift extends Character {

	public Rift(int lvl, Drawable drawable, int x, int y,
			GameInstance gameInstance, String name) {
		super(lvl, drawable, x, y, gameInstance, name);
		this.attackSpeed = 1;
	}
	
	public void autoAttack(GameObject target) {
		Character riftSpawn = new Character(1, new BitmapDrawable(BitmapFactory.decodeResource(gameInstance.gameActivity.getResources(), R.drawable.spider)),
				this.shape.getCenter().x, this.shape.getCenter().y - 250, gameInstance, "Rift Spawn");
		riftSpawn.setTarget(target);
		riftSpawn.attackSpeed = 1;
		riftSpawn.destination = target;
		riftSpawn.targetable = true;
		riftSpawn.maxHealth = 5;
		riftSpawn.remainingHealth = riftSpawn.maxHealth;
		gameInstance.addObject(riftSpawn);
		lastAttackTime = System.currentTimeMillis();
	}
}
