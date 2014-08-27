package com.rileyatodd.MageGame;

import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Rift extends Character {

	public Rift(int lvl, Bitmap bitmap, int x, int y,
			GameInstance gameInstance, String name) {
		super(lvl, bitmap, x, y, gameInstance, name);
		this.attackSpeed = 1;
	}
	
	public void autoAttack(GameObject target) {
		Character riftSpawn = new Character(1, BitmapFactory.decodeResource(gameInstance.gameActivity.getResources(), R.drawable.spider),
				this.shape.getCenter().x, this.shape.getCenter().y - 250, gameInstance, "Rift Spawn");
		riftSpawn.setTarget(target);
		riftSpawn.attackSpeed = 1;
		riftSpawn.destination = target;
		riftSpawn.targetable = true;
		riftSpawn.maxHealth = 5;
		riftSpawn.remainingHealth = riftSpawn.maxHealth;
		gameInstance.toAdd.add(riftSpawn);
		lastAttackTime = System.currentTimeMillis();
	}
}
