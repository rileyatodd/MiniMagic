package com.rileyatodd.MageGame.spells;

import android.annotation.SuppressLint;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.Point;
import com.rileyatodd.MageGame.core.Spell;
import com.rileyatodd.MageGame.core.Character;

public class SummonMinion extends Spell {

	private Character minion;
	
	public SummonMinion(Character minion, GameInstance gameInstance) {
		super(gameInstance);
		this.minion = minion;
		this.setBaseCooldown(5000);
	}
	
	@SuppressLint("UseValueOf")
	@Override
	public void onCast(Character caster, Point loc) {
		super.onCast(caster, loc); //Handle common stuff like cooldown management
		minion = new Character(minion);
		minion.setTarget(caster.getTarget());
		gameInstance.addObject(minion);
	}
	
}
