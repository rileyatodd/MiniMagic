package com.rileyatodd.MageGame;

import com.rileyatodd.MageGame.core.Circle;
import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;
import com.rileyatodd.MageGame.core.Point;

public class Destination extends GameObject {
	public GameObject destinationOf;
	
	public Destination(GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		super(gameInstance, drawable, loc, name);
		this.solid = false;
		this.shape = new Circle(this.shape.getCenter(), 1);
	}

	public void onCollide(GameObject object) {
		if (object.getDestination() == this) {
			this.despawn();
		}
	}
	
}
