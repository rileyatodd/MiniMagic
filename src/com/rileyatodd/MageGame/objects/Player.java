package com.rileyatodd.MageGame.objects;

import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.geometry.Point;


public class Player extends Character{
	
	public Player(int lvl, GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		super(lvl, gameInstance, drawable, loc, name);
		this.centered = true;
		this.remainingHealth = this.getMaxHealth() - 5;
	}
	
	@Override
	public boolean onCollision(GameObject object) {
		if (object == getDestination()) {
			object.despawn();
			return true;
		} else if (object instanceof Scenery) {
			return true;
		} else {
			return false;
		}
	}
	
}
