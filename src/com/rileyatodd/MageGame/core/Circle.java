package com.rileyatodd.MageGame.core;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Shape {

	public int radius;
	
	public Circle(Point center, int r) {
		super(center);
		this.radius = r;
	}
	
	@Override
	public boolean contains(Point point) {
		return radius*radius >= ((point.x - getCenter().x)*(point.x - getCenter().x) + (point.y - getCenter().y)*(point.y - getCenter().y));
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
