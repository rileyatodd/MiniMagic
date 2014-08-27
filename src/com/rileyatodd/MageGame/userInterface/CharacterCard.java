package com.rileyatodd.MageGame.userInterface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.Subject;

public class CharacterCard extends UIFrame {
	//private static final String TAG = CharacterCard.class.getSimpleName();
	private Paint healthBackgroundPaint = new Paint();
	private Paint healthBarPaint = new Paint();
	private Paint infoPaint = new Paint();
	private Paint infoBackgroundPaint = new Paint();
	private Rect healthBackground = new Rect();
	private Rect healthBar = new Rect();
	private Rect infoBackground = new Rect();
	private String info;
	private Character character;
	private Character targetOf;
	
	public CharacterCard(Rect bounds) {
		super(bounds);
		healthBackgroundPaint.setColor(Color.RED);
		healthBarPaint.setColor(Color.GREEN);
		infoBackgroundPaint.setColor(Color.BLACK);
		infoPaint.setTextAlign(Paint.Align.CENTER);
		infoPaint.setColor(Color.WHITE);
		healthBackground.set(bounds.left, bounds.top, bounds.right, bounds.top+25);
		infoBackground.set(bounds.left, bounds.top + 25, bounds.right, bounds.bottom);
		healthBar.set(healthBackground);
	}
	
	public void setCharacter(Character c) {
		if (c == null) {
			this.character = null;
		} else {
			this.character = c;
			character.attachObserver(this);
			updateSubject(character, "");
		}
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (character != null) {
			canvas.drawRect(healthBackground, healthBackgroundPaint);
			canvas.drawRect(healthBar, healthBarPaint);
			info = character.name + " " + character.lvl;
			canvas.drawRect(infoBackground, infoBackgroundPaint);
			canvas.drawText(info, bounds.right/2, healthBar.bottom + 20, infoPaint);
		}
	}
	
	public void updateSubject(Subject object, String message) {
		if (object == getTargetOf()) {
			Character p1 = (Character) object;
			setCharacter((Character) p1.getTarget());
		} else if (object instanceof Character) {
			Character character = (Character) object;
			int newRight = (int) (character.remainingHealth / (double) character.maxHealth * healthBackground.width());
			healthBar.set(healthBar.left, healthBar.top, newRight, healthBar.bottom);
		}
	}

	public Character getTargetOf() {
		return targetOf;
	}

	public void setTargetOf(Character targetOf) {
		this.targetOf = targetOf;
	}
}