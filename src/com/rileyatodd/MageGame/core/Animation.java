package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Animation {
	public int framesPerSecond = 30;
	public boolean running = false;
	long startTime;
	int index;
	public ArrayList<Bitmap> bitmaps;
	Paint paint;
	private int width;
	private int height;
	
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
