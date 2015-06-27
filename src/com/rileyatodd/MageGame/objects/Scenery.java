package com.rileyatodd.MageGame.objects;

import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.geometry.Point;

public class Scenery extends GameObject {
	public Scenery(GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		super(gameInstance, drawable, loc, name);
		targetable = true;
	}
}
