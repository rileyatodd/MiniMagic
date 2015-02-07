package com.rileyatodd.MageGame.spells;

import android.graphics.BitmapFactory;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.core.BitmapDrawable;
import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.Point;
import com.rileyatodd.MageGame.core.Projectile;
import com.rileyatodd.MageGame.core.Spell;


public class Fireball extends Spell {

	private int baseDamage = 3;
	
	public Fireball(GameInstance gameInstance) {
		super(gameInstance);
		// TODO Auto-generated constructor stub
		this.setBaseCooldown(1000); //1 second cooldown
		this.setRange(800); //Range in pixels, need to change this when I update the coordinate system.
		
	}
	
	@Override
	public void onCast(Character caster, Point loc) {
		super.onCast(caster, loc); //Handle common things like cooldown management
		if (caster.shape.getCenter().distanceTo(loc) < getRange()) {
			Drawable drawable = new BitmapDrawable(BitmapFactory.decodeResource(gameInstance.gameActivity.getResources(), R.drawable.fireball));
			Projectile fireball = new Projectile((int) (baseDamage * caster.getPower()), caster, caster.getTarget(), gameInstance, drawable, loc, "fireball");
			gameInstance.addObject(fireball);
		}
	}
}
