package com.rileyatodd.MageGame.userInterface;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.rileyatodd.MageGame.core.Drawable;

public class Button extends UIFrame {

	private ButtonCallback buttonCallback;
	private Drawable drawable;
	private String text;
	public Paint backgroundPaint;
	private Paint textPaint;
	private Paint drawablePaint;
	
	public Button(Rect bounds) {
		super(bounds);
		backgroundPaint = new Paint();
		backgroundPaint.setColor(Color.BLACK);
		textPaint = new Paint();
		textPaint.setTextAlign(Paint.Align.CENTER);
	}
	
	public Button(Rect bounds, String text, Paint backgroundPaint) {
		super(bounds);
		this.backgroundPaint = backgroundPaint;
		this.text = text;
		textPaint = new Paint();
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setColor(Color.WHITE);
	}
	
	public void addButtonCallback(ButtonCallback buttonCallback) {
		this.buttonCallback = buttonCallback;
	}
	
	public void onClick() {
		this.buttonCallback.onButtonPress(this);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		boolean retVal = false;
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (this.getBounds().contains(x,y) ) {
			this.onClick();
			retVal = true;
		}
		super.onTouchEvent(event);
		return retVal;
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (drawable != null) {
			drawable.draw(canvas, drawablePaint, getBounds().left, getBounds().top);
		} else {
			canvas.drawRect(getBounds(), backgroundPaint);
		}
		if (text != null) {
			canvas.drawText(text, getBounds().centerX(), getBounds().centerY(), textPaint);
		}
	}
	
	public void setText(String text) {
		this.text = text;
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		this.textPaint = paint;
	}
}
