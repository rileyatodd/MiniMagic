package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Animation implements Drawable {
	public boolean running = false;
	long lastFrame;
	int index;
	private ArrayList<Frame> frames;
	Paint paint;
	private int width;
	private int height;
	private int repetitions;
	private int repNum;
	
	public Animation(int repetitions) {
		index = 0;
		paint = new Paint();
		frames = new ArrayList<Frame>();
		this.repetitions = repetitions;
		repNum = 0;
	}
	
	public void start() {
		index = 0;
		lastFrame = System.currentTimeMillis();
		running = true;
	}
	
	public void update() {
		if (repNum == 0 && !running) {
			this.start();
			Log.d("Animation", "starting animation");
		}
		if (running) {
			long time = System.currentTimeMillis();
			Frame currentFrame = frames.get(index);
			if (time - lastFrame > currentFrame.duration) {
				index++;
				lastFrame = time;
			}
		}
		if (index >= frames.size()) {
			repNum++;
			index = 0;
			if (repNum == repetitions) {//Setting repNums to -1 induces infite looping 
				running = false;
				Log.d("Animation", "stopping animation");
			} 
		}
	}
	
	public void reset() {
		repNum = 0;
		index = 0;
	}
	
	public void draw(Canvas canvas, Paint paint, int x, int y) {
		this.update();
		if (running) {
			frames.get(index).drawable.draw(canvas, paint, x, y);
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
	
	public void addFrame(Drawable drawable, int duration) {
		frames.add(new Frame(drawable, duration));
	}
	
	public int getRepNum() {
		return repNum;
	}
	
	public int getRepetitions() {
		return repetitions;
	}
}

class Frame {
	Drawable drawable;
	int duration;
	
	Frame(Drawable drawable, int duration) {
		this.drawable = drawable;
		this.duration = duration;
	}
}
