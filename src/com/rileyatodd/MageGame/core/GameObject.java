package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.rileyatodd.MageGame.Destination;

public class GameObject implements Observer, Subject {
	
	//private static final String TAG = GameObject.class.getSimpleName();
	public GameInstance gameInstance;
	public boolean visible = true;
	public GameObject destination = null;
	public boolean centered = false;
	public float speed = 5;
	public boolean solid;
	private Drawable drawable;
	public Shape shape;
	public boolean targetable;
	public String name;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public String toString() {
		return name + "@ (" + this.shape.getCenter().x + ", " + this.shape.getCenter().y + ")";
	}
	
	public GameObject(Drawable drawable, int x, int y, GameInstance gameInstance, String name) {
		this.gameInstance = gameInstance;
		this.drawable = drawable;
		this.name = name;
		this.solid = true;
		if (drawable == null) {
			this.shape = new Circle(x,y, 10);
			this.drawable = shape;
		} else {
			this.shape = new Rectangle(x,y, drawable.getWidth(), drawable.getHeight());
		}
		this.targetable = false;
	}
	
	public void move(int x, int y) {
		this.shape.setCenter(x, y);
	}
		
	public void update() {
		//If the object is moving calculate dx/dy, detect collisions, and then recalculate dx/dy accordingly
		double dx = 0;
		double dy = 0;
		if (destination != null) {
			//vertical and horizontal distances to the destination
			int xDistance = destination.shape.getCenter().x - this.shape.getCenter().x;
			int yDistance = destination.shape.getCenter().y - this.shape.getCenter().y;

			//Check for divBy0
			if(xDistance == 0) {
				dx = 0;
				if (yDistance == 0) {
					dy = 0;
				} else {
					dy = (yDistance / Math.abs(yDistance)) * speed;
				}
			} else {
				float m = (float)yDistance/xDistance;
				dx = (xDistance / Math.abs(xDistance)) * (Math.sqrt(speed*speed / (m*m + 1)));
				dy = (m * dx);
			}

			//search for collisions and perform any found with this.collide()
			ArrayList<boolean[]> collisions = gameInstance.detectCollisions(this, dx, dy);
			double[] dp = collide(collisions, dx, dy);
			dx = dp[0];
			dy = dp[1];

		}
		
		//With correct coordinates determined, move the object
		this.move((int)(this.shape.getCenter().x + dx), (int)(this.shape.getCenter().y + dy));
		
		//move the camera if necessary
		if (this.centered) {
			gameInstance.viewCoordX = this.shape.getCenter().x - gameInstance.width / 2;
			gameInstance.viewCoordY = this.shape.getCenter().y - gameInstance.height / 2;
		}
	}
	
	public void despawn() {
		notifyObservers("despawn");
		gameInstance.removeObject(this);
	}
	
	//Implementation of Observer
	public void updateSubject(Subject sub, String message) {
		if (message.equals("despawn")) {
			if (sub == destination) {
				destination = null;
			}
		}
	}
	
	//Implementation of Subject
	public void attachObserver(Observer observer) {
		this.observers.add(observer);
	}
	
	public void detachObserver(Observer observer) {
		this.observers.remove(observer);
	}

	public void notifyObservers(String message) {
		for (int i = observers.size() - 1; i >= 0; i--) {
			Observer observer = observers.get(i);
			observer.updateSubject(this, message);
		}
	}
	
	
	//To Do: generalize to work for circles
	public boolean contains(double x, double y) {
		return this.shape.contains(x,y);
	}
	
	public double[] collide(ArrayList<boolean[]> collisions, double dx, double dy) {
		double[] returnVal = { dx, dy };
		//If object is solid, adjust dx and dy appropriately so that you don't move through it
		for(boolean[] collision: collisions) {
			if (collision[0]) {
				returnVal[0] = 0;
			}
			if (collision[1]) {
				returnVal[1] = 0;
			}
		}
		
		return returnVal;
	}
	
	public void draw(Canvas canvas, int viewCoordX, int viewCoordY) {
		if (visible) {
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.FILL);
			drawable.draw(canvas, paint, this.shape.getCenter().x - viewCoordX, this.shape.getCenter().y - viewCoordY);
		}
	}
	
	public void onCollision(GameObject object) {
		
	}
	
	public Destination createDestination(int x, int y, GameInstance gameInstance, String name) {
		Destination newDestination = new Destination(x,y,gameInstance,name);
		destination = newDestination;
		destination.attachObserver(this);
		return newDestination;
	}
	
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
		this.shape = new Rectangle(this.shape.getCenter().x, this.shape.getCenter().y, drawable.getWidth(), drawable.getHeight());
	}
	
	public Drawable getDrawable() {
		return drawable;
	}
}
