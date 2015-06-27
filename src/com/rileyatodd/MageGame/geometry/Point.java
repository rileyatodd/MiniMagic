package com.rileyatodd.MageGame.geometry;

public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point loc) {
		this.x = loc.x;
		this.y = loc.y;
	}

	public double distanceTo(Point other) {
		int dx = other.x - this.x;
		int dy = other.y - this.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
