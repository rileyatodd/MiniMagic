package com.rileyatodd.MageGame;

import android.graphics.Bitmap;

public class Player extends Character{
	
	public Player(int lvl, Bitmap bitmap, int x, int y, GameInstance gameInstance, String name) {
		super(lvl, bitmap, x, y, gameInstance, name);
		this.centered = true;
		this.attackSpeed = 5;
		this.remainingHealth = this.maxHealth - 5;
	}
	
	
	public double[] collide(GameObject object, int collisionKey, double dx, double dy) {
		double[] returnVal = { dx , dy };
		
		returnVal = super.collide(object, collisionKey, dx, dy);
		
		if (object == destination) {
			object.despawn();
		}
		
		return returnVal;
	}
	
	
}
