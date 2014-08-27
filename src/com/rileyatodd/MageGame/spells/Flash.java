package com.rileyatodd.MageGame.spells;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.core.Animation;
import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;
import com.rileyatodd.MageGame.core.Spell;
import com.rileyatodd.MageGame.userInterface.ButtonCallback;

public class Flash extends Spell implements ButtonCallback {

	public Flash(GameObject caster, GameObject destination, Bitmap bitmap,
			int x, int y, GameInstance gameInstance, String name) {
		super(caster, destination, bitmap, x, y, gameInstance, name);
		paint.setColor(Color.YELLOW);
		this.range = 500;
		this.animated = true;
		Animation animationT = new Animation();
		animationT.framesPerSecond = 15;
		Bitmap lightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightning);
		Bitmap reverseLightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightningreverse);
		animationT.bitmaps.add(lightning);
		animationT.bitmaps.add(reverseLightning);
		animationT.bitmaps.add(lightning);
		animationT.bitmaps.add(reverseLightning);
		animationT.setWidth(animationT.bitmaps.get(0).getWidth());
		animationT.setHeight(animationT.bitmaps.get(0).getHeight());
		this.setAnimation(animationT);
	}
	
	public void onActivate(Character caster, int x, int y) {
		gameInstance.gameActivity.gameView.selectedSpell = this;
	}
	
	public void onCast(Character caster, int x, int y) {
		if ((x - caster.shape.getCenter().x)*(x - caster.shape.getCenter().x) + 
				(y - caster.shape.getCenter().y)*(y - caster.shape.getCenter().y) <= 
				range*range) {
			if (caster.destination != null) {
				caster.destination.despawn();
			}
			caster.move(x, y);
			gameInstance.gameActivity.gameView.selectedSpell = null;
			this.move(x, y);
			gameInstance.toAdd.add(this);
			animation.start();
			Log.d("Flash", "Used");
		}
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
