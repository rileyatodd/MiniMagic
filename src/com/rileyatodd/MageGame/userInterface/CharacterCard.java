package com.rileyatodd.MageGame.userInterface;

import com.rileyatodd.MageGame.core.Character;
import com.rileyatodd.MageGame.core.GameObject;
import com.rileyatodd.MageGame.core.Subject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class CharacterCard extends UIFrame {
	//private static final String TAG = CharacterCard.class.getSimpleName();
	public Paint healthBackgroundPaint = new Paint();
	public Paint healthBarPaint = new Paint();
	public Paint infoPaint = new Paint();
	public Rect healthBackground = new Rect();
	public Rect healthBar = new Rect();
	public String info;
	public Character character;
	public Character targetOf;
	
	public CharacterCard(Rect bounds) {
		super(bounds);
		healthBackgroundPaint.setColor(Color.RED);
		healthBarPaint.setColor(Color.GREEN);
		infoPaint.setTextAlign(Paint.Align.CENTER);
		infoPaint.setColor(Color.WHITE);
		healthBackground.set(bounds.left, bounds.top, bounds.right, bounds.top+25);
		healthBar.set(healthBackground);
	}
	
	public void setCharacter(Character c) {
		if (c == null) {
			this.character = null;
		} else {
			this.character = c;
			character.attachObserver(this);
			updateSubject(character);
		}
	}
	
	public void draw(Canvas canvas) {
		if (character != null) {
			canvas.drawRect(healthBackground, healthBackgroundPaint);
			canvas.drawRect(healthBar, healthBarPaint);
			info = character.name + " " + character.lvl;
			//Log.d(TAG, "text coords: (" + healthBar.left + ", " + healthBar.bottom +")");
			canvas.drawText(info, bounds.right/2, healthBar.bottom + 20, infoPaint);
		}
	}
	
	public void updateSubject(Subject object) {
		if (object == targetOf) {
			Character p1 = (Character) object;
			setCharacter((Character) p1.getTarget());
		} else if (object instanceof Character) {
			Character character = (Character) object;
			int newRight = (int) (character.remainingHealth / (double) character.maxHealth * healthBackground.width());
			//Log.d(TAG, "newRight: " + newRight);
			healthBar.set(healthBar.left, healthBar.top, newRight, healthBar.bottom);
			
		}
	}
	
	public void detachObserved(GameObject object) {
		if (object == character) {
			character = null;
		} else if (object == targetOf) {
			targetOf = null;
		}
	}
}