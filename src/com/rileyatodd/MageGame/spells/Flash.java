package com.rileyatodd.MageGame.spells;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.core.Animation;
import com.rileyatodd.MageGame.core.BitmapDrawable;
import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.GameObject;
import com.rileyatodd.MageGame.core.Spell;
import com.rileyatodd.MageGame.userInterface.ButtonCallback;

public class Flash extends Spell implements ButtonCallback {

	public Flash(GameObject caster, GameObject destination, Drawable drawable,
			int x, int y, GameInstance gameInstance, String name) {
		super(caster, destination, drawable, x, y, gameInstance, name);
		paint.setColor(Color.YELLOW);
		this.range = 500;
		Animation animationT = new Animation(1);
		Bitmap lightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightning);
		Bitmap reverseLightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightningreverse);
		animationT.addFrame(new BitmapDrawable(lightning), 100);
		animationT.addFrame(new BitmapDrawable(reverseLightning), 100);
		animationT.addFrame(new BitmapDrawable(lightning), 200);
		animationT.addFrame(new BitmapDrawable(reverseLightning), 100);
		animationT.setWidth(lightning.getWidth());
		animationT.setHeight(lightning.getHeight());
		this.setDrawable(animationT);
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
			gameInstance.addObject(this);
			Log.d("Flash", "Used");
		}
	}

	public void update() {
		//If the animation is not running, remove from gameObjects	
		Animation animation = (Animation)this.getDrawable();
		if (!animation.running && animation.getRepNum() >= animation.getRepetitions()) {
			gameInstance.removeObject(this);
			animation.reset();
		} else {
			super.update();
		}
	}
}
