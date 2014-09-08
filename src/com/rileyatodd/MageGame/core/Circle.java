package com.rileyatodd.MageGame.core;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Shape {

	public int radius;
	
	public Circle(int x, int y, int r) {
		super(x,y);
		this.radius = r;
	}
	
	@Override
	public boolean contains(double x, double y) {
		return radius*radius >= ((x - getCenter().x)*(x - getCenter().x) + (y - getCenter().y)*(y - getCenter().y));
	}

	@Override
	public void draw(Canvas canvas, Paint paint, int x, int y) {
		canvas.drawCircle(x, y, this.radius, paint);
	}

	@Override
	public int getHeight() {
		return radius;
	}

	@Override
	public int getWidth() {
		return radius;
	}
	
}
