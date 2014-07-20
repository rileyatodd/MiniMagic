package com.rileyatodd.MageGame;

import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;
import com.rileyatodd.MageGame.core.Subject;

import android.graphics.Bitmap;

public class Scenery extends GameObject {
	public Scenery(Bitmap bitmap, int x, int y, GameInstance gameInstance, String name) {
		super(bitmap, x, y, gameInstance, name);
		targetable = true;
	}

	public void updateSubject(Subject observed) {
		// TODO Auto-generated method stub
		
	}

}
