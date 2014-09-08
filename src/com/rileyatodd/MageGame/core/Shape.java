package com.rileyatodd.MageGame.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public abstract class Shape implements Drawable {
	private Point center;
	
	public Shape(int x, int y) {
		center = new Point(x,y);
	}
	
	public void setCenter(int x, int y) {
		center.set(x, y);
	}
	
	public Point getCenter() {
		return center;
	}
	
	public abstract void draw(Canvas canvas, Paint paint, int x, int y);
	
	public abstract int getHeight();
	
	public abstract int getWidth();
	
	public abstract boolean contains(double x, double y);
}
