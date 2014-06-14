package com.rileyatodd.MageGame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

public class Flash extends Spell implements ButtonCallback {

	public Flash(GameObject caster, GameObject destination, Bitmap bitmap,
			int x, int y, GameInstance gameInstance, String name) {
		super(caster, destination, bitmap, x, y, gameInstance, name);
		paint.setColor(Color.YELLOW);
		this.range = 500;
		this.animated = true;
		this.animation = new Animation();
		this.animation.framesPerSecond = 15;
		Bitmap lightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightning);
		Bitmap reverseLightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightningreverse);
		this.objectHeight = lightning.getHeight();
		this.objectWidth = lightning.getWidth();
		this.animation.bitmaps.add(lightning);
		this.animation.bitmaps.add(reverseLightning);
		this.animation.bitmaps.add(lightning);
		this.animation.bitmaps.add(reverseLightning);
	}
	
	public void onCast(Character caster, int x, int y) {
		if (Math.sqrt((x - caster.screenX)*(x - caster.screenX) + (y - caster.screenY)*(y - caster.screenY)) <= range) {
			if (caster.destination != null) {
				caster.destination.despawn();
			}
			caster.move(x + gameInstance.viewCoordX, y + gameInstance.viewCoordY);
			gameInstance.gameActivity.gameView.selectedSpell = null;
			this.move(x + gameInstance.viewCoordX, y + gameInstance.viewCoordY);
			gameInstance.toAdd.add(this);
			animation.start();
			Log.d("Flash", "Used");
		}
	}
	
	public void onButtonPress() {
		gameInstance.gameActivity.gameView.selectedSpell = this;
		Log.d("Flash", "Selected");
	}
	
	public void update() {
		//If the animation is not running, remove from gameObjects
		if (animation.running) {
			super.update();
		} else {
			gameInstance.toRemove.add(this);
		}
	}
}
