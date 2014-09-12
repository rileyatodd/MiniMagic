package com.rileyatodd.MageGame.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;


public class BitmapDrawable implements Drawable {
	Bitmap bitmap;

	public BitmapDrawable(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint, int x, int y) {
		canvas.drawBitmap(bitmap, x - getWidth()/2, y - getHeight()/2, paint);
	}

	@Override
	public int getWidth() {
		return bitmap.getWidth();
	}

	@Override
	public int getHeight() {
		return bitmap.getHeight();
	}
	
}
