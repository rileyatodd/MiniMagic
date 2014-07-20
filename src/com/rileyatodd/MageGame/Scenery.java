package com.rileyatodd.MageGame;

import android.graphics.Bitmap;

import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;

public class Scenery extends GameObject {
	public Scenery(Bitmap bitmap, int x, int y, GameInstance gameInstance, String name) {
		super(bitmap, x, y, gameInstance, name);
		targetable = true;
	}

}
