package com.rileyatodd.MageGame.core;

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
	
}
