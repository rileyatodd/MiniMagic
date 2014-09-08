package com.rileyatodd.MageGame.core;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Drawable {
	public void draw(Canvas canvas, Paint paint, int x, int y);
	
	public int getWidth();
	
	public int getHeight();
}
