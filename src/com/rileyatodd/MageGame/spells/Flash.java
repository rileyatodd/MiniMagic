package com.rileyatodd.MageGame.spells;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.core.Animation;
import com.rileyatodd.MageGame.core.BitmapDrawable;
import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.Point;
import com.rileyatodd.MageGame.core.Spell;
import com.rileyatodd.MageGame.core.VisualEffect;
import com.rileyatodd.MageGame.userInterface.ButtonCallback;

public class Flash extends Spell implements ButtonCallback {

	private VisualEffect visual;
	
	public Flash(GameInstance gameInstance) {
		super(gameInstance);
		getPaint().setColor(Color.YELLOW);
		this.setRange(500);
		Animation animationT = new Animation(1);
		Bitmap lightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightning);
		Bitmap reverseLightning = BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.lightningreverse);
		animationT.addFrame(new BitmapDrawable(lightning), 100);
		animationT.addFrame(new BitmapDrawable(reverseLightning), 100);
		animationT.addFrame(new BitmapDrawable(lightning), 200);
		animationT.addFrame(new BitmapDrawable(reverseLightning), 100);
		animationT.setWidth(lightning.getWidth());
		animationT.setHeight(lightning.getHeight());
		visual = new VisualEffect(gameInstance, animationT, new Point(0,0), "Flash Animation");
	}
	
	@Override
	public void onActivate(Character caster, Point loc) {
		gameInstance.setSelectedSpell(this);
	}
	@Override
	public void onCast(Character caster, Point loc) {
		if (loc.distanceTo(caster.shape.getCenter()) < getRange()) {
			if (caster.getDestination() != null) {
				caster.getDestination().despawn();
			}
			caster.move(loc);
			gameInstance.setSelectedSpell(null);
			visual.move(loc);
			gameInstance.addObject(visual);
			Log.d("Flash", "Used");
		}
	}

}
