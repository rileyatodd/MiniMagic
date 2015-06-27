package com.rileyatodd.MageGame.objects;

import java.util.ArrayList;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.core.BitmapDrawable;
import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.Observer;
import com.rileyatodd.MageGame.core.Subject;
import com.rileyatodd.MageGame.geometry.Circle;
import com.rileyatodd.MageGame.geometry.Point;
import com.rileyatodd.MageGame.geometry.Rectangle;
import com.rileyatodd.MageGame.geometry.Shape;

public class GameObject implements Observer, Subject {
	
	//private static final String TAG = GameObject.class.getSimpleName();
	public boolean visible = true;
	private GameObject destination = null;
	public GameInstance gameInstance;
	public boolean centered = false;
	public float speed = 5;
	public boolean solid;
	private Drawable drawable;
	private Paint drawablePaint;
	public Shape shape;
	public boolean targetable;
	public String name;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public String toString() {
		return name + "@ (" + this.shape.getCenter().x + ", " + this.shape.getCenter().y + ")";
	}
	
	public GameObject(GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		this.gameInstance = gameInstance;
		this.drawable = drawable;
		this.name = name;
		this.solid = true;
		if (drawable == null) {
			this.shape = new Circle(loc, 10);
			this.drawable = shape;
		} else {
			this.shape = new Rectangle(loc, drawable.getWidth(), drawable.getHeight());
		}
		this.targetable = false;
		this.drawablePaint = new Paint();
		drawablePaint.setColor(Color.BLACK);
		drawablePaint.setStyle(Paint.Style.FILL);
	}
	
	public void move(Point loc) {
		this.shape.setCenter(loc);
	}
	
	public void moveBy(double dx, double dy) {
		this.shape.setCenter(new Point((int)(this.shape.getCenter().x + dx), (int)(this.shape.getCenter().y + dy)));
	}
	
	long longestUpdateTime = 0;
	public void update() {
		
		//If the object is moving calculate dx/dy, detect collisions, and then recalculate dx/dy accordingly
		double dx = 0;
		double dy = 0;
		if (getDestination() != null) {
			//vertical and horizontal distances to the destination
			int xDistance = getDestination().shape.getCenter().x - this.shape.getCenter().x;
			int yDistance = getDestination().shape.getCenter().y - this.shape.getCenter().y;

			//Check for divBy0
			if(xDistance == 0) {
				dx = 0;
				if (yDistance == 0) {
					dy = 0;
				} else {
					dy = (yDistance / Math.abs(yDistance)) * speed;
				}
			} else if (yDistance == 0) {
				dx = (xDistance / Math.abs(xDistance)) * speed;
			} else {
				float m = (float)yDistance/xDistance;
				dx = (xDistance / Math.abs(xDistance)) * (Math.sqrt(speed*speed / (m*m + 1)));
				dy = (m * dx);
			}

			//search for collisions and perform any found with this.collide()
			ArrayList<boolean[]> collisions = gameInstance.detectCollisions(this, dx, dy);
			double[] dp = preventCollision(collisions, dx, dy);
			dx = dp[0];
			dy = dp[1];
			
		}
		
		//With correct coordinates determined, move the object
		this.moveBy(dx, dy);
		
		//move the camera if necessary
		if (this.centered) {
			gameInstance.getViewLoc().x = this.shape.getCenter().x - gameInstance.width / 2;
			gameInstance.getViewLoc().y = this.shape.getCenter().y - gameInstance.height / 2;
		}
		
	}
	
	public void despawn() {
		notifyObservers("despawn");
		gameInstance.removeObject(this);
	}
	
	//Implementation of Observer
	public void updateSubject(Subject sub, String message) {
		if (message.equals("despawn")) {
			if (sub == getDestination()) {
				setDestination(null);
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
	
	public double[] preventCollision(ArrayList<boolean[]> collisions, double dx, double dy) {
		double[] returnVal = { dx, dy };
		//adjust dx and dy appropriately so that you don't move through it
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
	
	public void draw(Canvas canvas, Point viewLoc) {
		if (visible) {
			drawable.draw(canvas, drawablePaint, this.shape.getCenter().x - viewLoc.x, this.shape.getCenter().y - viewLoc.y);
		}
	}
	
	public boolean onCollision(GameObject object) {
		if (object.solid || this.solid) {
			return true;
		} else {
			return false;
		}
	}
	
	public Destination createDestination(int x, int y, GameInstance gameInstance, String name) {
		Destination newDestination = new Destination(gameInstance, new BitmapDrawable(BitmapFactory.decodeResource(gameInstance.gameActivity.resources, R.drawable.crosshairs)), new Point(x,y),name);
		setDestination(newDestination);
		getDestination().attachObserver(this);
		return newDestination;
	}
	
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
		this.shape = new Rectangle(this.shape.getCenter(), drawable.getWidth(), drawable.getHeight());
	}
	
	public Drawable getDrawable() {
		return drawable;
	}

	public GameObject getDestination() {
		return destination;
	}

	public void setDestination(GameObject destination) {
		this.destination = destination;
	}
}
