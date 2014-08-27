package com.rileyatodd.MageGame.core;

import android.graphics.Point;

public abstract class Shape {
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
	
	public abstract boolean contains(double x, double y);
}
