package com.rileyatodd.MageGame;

import android.graphics.Bitmap;
import android.graphics.Color;

public class DivineGrace extends Spell {

	public DivineGrace(GameObject caster, GameObject destination,
			Bitmap bitmap, int x, int y, GameInstance gameInstance, String name) {
		super(caster, destination, bitmap, x, y, gameInstance, name);
		paint.setColor(Color.WHITE);
	}
	
	public void onCast(Character caster, int x, int y) {
		healTarget(caster, 10);
		for (PlayerEffect effect : caster.activeEffects) {
			if (effect.name == "DivineGrace") {
				return;
			}
		}
		PlayerEffect speedBoost = new PlayerEffect(5.0, caster, "DivineGrace");
		speedBoost.activate();
	}
	
}
