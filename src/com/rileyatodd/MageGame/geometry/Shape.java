package com.rileyatodd.MageGame.geometry;

import com.rileyatodd.MageGame.core.Drawable;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Shape implements Drawable {
	private Point center;
	
	public Shape(Point center) {
		this.center = center;
	}
	
	public void setCenter(Point loc) {
		center.set(loc.x, loc.y);
	}
	
	public Point getCenter() {
		return center;
	}
	
	public abstract void draw(Canvas canvas, Paint paint, int x, int y);
	
	public abstract int getHeight();
	
	public abstract int getWidth();
	
	public abstract boolean contains(Point point);
}
