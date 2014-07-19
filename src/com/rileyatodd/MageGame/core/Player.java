package com.rileyatodd.MageGame.core;


import android.graphics.Bitmap;

public class Player extends Character{
	
	public Player(int lvl, Bitmap bitmap, int x, int y, GameInstance gameInstance, String name) {
		super(lvl, bitmap, x, y, gameInstance, name);
		this.centered = true;
		this.attackSpeed = 5;
		this.remainingHealth = this.maxHealth - 5;
	}
	
	
	public void onCollision(GameObject object) {
		if (object == destination) {
			object.despawn();
		}
	}
	
}
