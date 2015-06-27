package com.rileyatodd.MageGame.spells;

import android.graphics.Color;
import android.graphics.Paint;

import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.geometry.Point;
import com.rileyatodd.MageGame.objects.Character;
import com.rileyatodd.MageGame.userInterface.Button;
import com.rileyatodd.MageGame.userInterface.ButtonCallback;

public class Spell implements ButtonCallback {

	protected GameInstance gameInstance;
	private Paint paint;
	private int baseCooldown; //Cooldown time in milliseconds. For auto attacks this dictates the speed at which they are cast.
	private int actualCooldown; //Effective CD after haste etc.
	private long lastCastTime;
	private int range;
	
	public Spell(GameInstance gameInstance) {
		this.gameInstance = gameInstance;
		setPaint(new Paint());
		getPaint().setColor(Color.WHITE);
	}
	
	public void onButtonPress(Button button) {
		onActivate(gameInstance.player1, new Point(0,0));
	}
	
	public void onActivate(Character caster, Point loc) {
		onCast(caster, loc);
	}
	
	public void onCast(Character caster, Point loc) {
		actualCooldown = (int)(getBaseCooldown() / caster.getHaste());
		lastCastTime = System.currentTimeMillis();
	}
	
	public void healTarget(Character target, int amount) {
		if (amount < target.getMaxHealth() - target.remainingHealth) {
			target.remainingHealth += amount;
		} else {
			target.remainingHealth = target.getMaxHealth();
		}
		target.notifyObservers("health");
	}
	
	public void damageTarget(Character target, int amount) {
		target.remainingHealth -= amount;
		target.notifyObservers("health");
	}
	

	
	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public int getCooldown() {
		return actualCooldown;
	}

	public long getRemainingCooldown() {
		return actualCooldown - (System.currentTimeMillis() - lastCastTime);
	}
	
	public void setCooldown(int cooldown) {
		this.actualCooldown = cooldown;
	}
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getBaseCooldown() {
		return baseCooldown;
	}

	public void setBaseCooldown(int baseCooldown) {
		this.baseCooldown = baseCooldown;
	}
}
