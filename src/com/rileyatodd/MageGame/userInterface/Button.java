package com.rileyatodd.MageGame.userInterface;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Button extends UIFrame {

	private ButtonCallback buttonCallback;
	private Bitmap bitmap;
	private String text;
	public Paint backgroundPaint;
	private Paint textPaint;
	private Paint bitmapPaint;
	
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

		if (this.bounds.contains(x,y) ) {
			this.onClick();
			retVal = true;
		}
		super.onTouchEvent(event);
		return retVal;
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, bounds.left, bounds.top, bitmapPaint);
		} else {
			canvas.drawRect(bounds, backgroundPaint);
		}
		if (text != null) {
			canvas.drawText(text, bounds.left + (bounds.right - bounds.left)/2, bounds.top + (bounds.bottom - bounds.top)/2, textPaint);
		}
	}
}
