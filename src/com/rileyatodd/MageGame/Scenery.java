package com.rileyatodd.MageGame;

import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;

public class Scenery extends GameObject {
	public Scenery(Drawable drawable, int x, int y, GameInstance gameInstance, String name) {
		super(drawable, x, y, gameInstance, name);
		targetable = true;
	}

}
