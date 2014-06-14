package com.rileyatodd.MageGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Button extends UIFrame {

	public ButtonCallback buttonCallback;
	public Bitmap bitmap;
	public String text;
	public Paint paint;
	
	public Button(Rect bounds) {
		super(bounds);
		paint = new Paint();
		paint.setColor(Color.BLACK);
	}
	
	public void addButtonCallback(ButtonCallback buttonCallback) {
		this.buttonCallback = buttonCallback;
	}
	
	public void onClick() {
		this.buttonCallback.onButtonPress();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		boolean retVal = false;
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (this.bounds.contains(x,y) ) {
			this.onClick();
			retVal = true;
		}
		super.onTouchEvent(event);
		return retVal;
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		canvas.drawRect(bounds, paint);
	}
}
