package com.rileyatodd.MageGame;

import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;
import com.rileyatodd.MageGame.core.Subject;

import android.graphics.BitmapFactory;

public class Destination extends GameObject {
	public GameObject destinationOf;
	
	public Destination(GameObject destinationOf, int x, int y, GameInstance gameInstance, String name) {
		super(BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.crosshairs), x, y, gameInstance, name);
		this.attachObserver(destinationOf);
		this.destinationOf = destinationOf;
		this.solid = false;
	}

	public void onCollide(GameObject object) {
		if (object.destination == this) {
			this.despawn();
		}
	}
	
	public void updateSubject(Subject observed) {
		// TODO Auto-generated method stub
		
	}

	public void detachSubject(Subject observed) {
		// TODO Auto-generated method stub
		
	}
	
	public void despawn() {
		destinationOf.destination = null;
		super.despawn();
	}
	
}
