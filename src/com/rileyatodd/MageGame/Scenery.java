package com.rileyatodd.MageGame;

import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;
import com.rileyatodd.MageGame.core.Point;

public class Scenery extends GameObject {
	public Scenery(GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		super(gameInstance, drawable, loc, name);
		targetable = true;
	}
}
