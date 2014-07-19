package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Animation {
	public int framesPerSecond = 30;
	public boolean running = false;
	long startTime;
	int index;
	public ArrayList<Bitmap> bitmaps;
	Paint paint;
	
	public Animation() {
		index = 0;
		paint = new Paint();
		bitmaps = new ArrayList<Bitmap>();
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
		running = true;
	}
	
	public void update() {
		index = (int) ((System.currentTimeMillis() - startTime) / 1000.0 * framesPerSecond);
		Log.d("Animation", "Updated index: " + index);
		if (index >= bitmaps.size()) {
			running = false;
		}
	}
	
	public void draw(Canvas canvas, int x, int y) {
		this.update();
		if (running) {
			canvas.drawBitmap(bitmaps.get(index), x, y, paint);
		}
	}
}
