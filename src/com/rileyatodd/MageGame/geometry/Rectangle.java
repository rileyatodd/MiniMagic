package com.rileyatodd.MageGame.geometry;


import android.graphics.Canvas;
import android.graphics.Paint;

public class Rectangle extends Shape {
	private int width;
	private int height;
	
	public Rectangle(Point center, int width, int height) {
		super(center);
		this.width = width;
		this.height = height;
	}
	
	public boolean contains(Point point) {
		int left = this.getCenter().x - width/2;
		int top = this.getCenter().y - height/2;
		if (point.x > left && 
				point.x < left + width &&
				point.y > top  &&
				point.y < top + height) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getLeft() {
		return getCenter().x - width/2;
	}
	
	public int getRight() {
		return getCenter().x + width/2;
	}
	
	public int getTop() {
		return getCenter().y - height/2;
	}
	
	public int getBottom() {
		return getCenter().y + height/2;
	}

	@Override
	public void draw(Canvas canvas, Paint paint, int x, int y) {
		canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), paint);
		
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}
}
