package com.rileyatodd.MageGame;

import android.graphics.BitmapFactory;

import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;

public class Destination extends GameObject {
	public GameObject destinationOf;
	
	public Destination(int x, int y, GameInstance gameInstance, String name) {
		super(BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.crosshairs), x, y, gameInstance, name);
		this.solid = false;
	}

	public void onCollide(GameObject object) {
		if (object.destination == this) {
			this.despawn();
		}
	}
	
}
