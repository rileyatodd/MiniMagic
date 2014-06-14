package com.rileyatodd.MageGame;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Animation {
	int framesPerSecond = 30;
	boolean running = false;
	long startTime;
	int index;
	ArrayList<Bitmap> bitmaps;
	Paint paint;
	
	Animation() {
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
