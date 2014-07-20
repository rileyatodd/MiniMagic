package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject implements Observer, Subject {
	
	//private static final String TAG = GameObject.class.getSimpleName();
	public GameInstance gameInstance;
	public boolean visible = true;
	public GameObject destination = null;
	public boolean centered = false;
	public float speed = 5;
	public boolean solid;
	public boolean animated = false;
	public Bitmap bitmap;
	public Animation animation;
	public int screenX;
	public int screenY;
	public int absoluteX;
	public int absoluteY;
	public int objectWidth;
	public int objectHeight;
	public boolean targetable;
	public String name;
	public ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public String toString() {
		return name + "@ (" + this.absoluteX + ", " + this.absoluteY + ")";
	}
	
	public GameObject(Bitmap bitmap, int x, int y, GameInstance gameInstance, String name) {
		this.gameInstance = gameInstance;
		this.bitmap = bitmap;
		this.name = name;
		this.solid = true;
		if (bitmap == null) {
			this.objectHeight = 0;
			this.objectWidth = 0;
		} else {
			this.objectHeight = bitmap.getHeight();
			this.objectWidth = bitmap.getWidth();
		}
		this.absoluteX = x;
		this.absoluteY = y;
		this.screenX = this.absoluteX - gameInstance.viewCoordX;
		this.screenY = this.absoluteY - gameInstance.viewCoordY;
		this.targetable = false;
	}
	
	public void move(int x, int y) {
		this.absoluteX = x;
		this.absoluteY = y;
		this.screenX = this.absoluteX - gameInstance.viewCoordX;
		this.screenY = this.absoluteY - gameInstance.viewCoordY; 
	}
		
	public void update() {
		//If the object is moving calculate dx/dy, detect collisions, and then recalculate dx/dy accordingly
		double dx = 0;
		double dy = 0;
		if (destination != null) {
			//vertical and horizontal distances to the destination
			int xDistance = destination.absoluteX - this.absoluteX;
			int yDistance = destination.absoluteY - this.absoluteY;

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
		this.move((int)(this.absoluteX + dx), (int)(this.absoluteY + dy));
		
		//move the camera if necessary
		if (this.centered) {
			//Log.d(TAG, "" + gameInstance.width);
			gameInstance.viewCoordX = absoluteX - gameInstance.width / 2;
			gameInstance.viewCoordY = absoluteY - gameInstance.height / 2;
			//Log.d(TAG, "CenterCoords: (" + absoluteX + ", " + absoluteY +")");
			//Log.d(TAG, "viewCoords: (" + gameInstance.getGameView().viewCoordX + ", " + gameInstance.getGameView().viewCoordY +")");
			//Log.d(TAG, "dx: " + dx + '\n' + "dy: " + dy);
		}
	}
	
	public void despawn() {
		notifyObservers("despawn");
		gameInstance.toRemove.add(this);
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
		if (x > this.absoluteX - this.objectWidth / 2 && 
				x < this.absoluteX + this.objectWidth / 2 &&
				y > this.absoluteY - this.objectHeight / 2  &&
				y < this.absoluteY + this.objectHeight / 2) {
			return true;
		} else {
			return false;
		}
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
	
	public void draw(Canvas canvas) {
		if (visible) {
			if (animated) {
				animation.draw(canvas, this.screenX - objectWidth / 2, this.screenY - objectHeight / 2);
			} else {
				canvas.drawBitmap(bitmap, screenX - objectWidth / 2,
						screenY - objectHeight / 2, null);
			}
		}
	}
	
	public void onCollision(GameObject object) {
		
	}
}
