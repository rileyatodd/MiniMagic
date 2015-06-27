package com.rileyatodd.MageGame.spells;

import android.graphics.Color;

import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.PlayerEffect;
import com.rileyatodd.MageGame.geometry.Point;
import com.rileyatodd.MageGame.objects.Character;

public class DivineGrace extends Spell {

	public DivineGrace(GameInstance gameInstance) {
		super(gameInstance);
		getPaint().setColor(Color.WHITE);
	}
	
	@Override
	public void onCast(Character caster, Point loc) {
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
