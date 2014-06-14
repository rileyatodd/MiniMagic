package com.rileyatodd.MageGame;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject implements Subject, Observer {
	
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
		this.solid = false;
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
			//Log.d(TAG, "dx: " + dx + '\n' + "dy: " + dy);

			//search for collisions and perform any found with this objects collide method
			for (int i = 0; i < gameInstance.gameObjects.size(); i++) {
				GameObject object = gameInstance.gameObjects.get(i);
				int collisionKey = detectCollision(object, dx, dy);
				if (collisionKey > 0) {
					double[] collision = collide(object, collisionKey, dx, dy);
					dx = collision[0];
					dy = collision[1];
				}
			}
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
		for (int i = observers.size() - 1; i >= 0; i--) {
			Observer observer = observers.get(i);
			this.detachObserver(observer);
		}
		gameInstance.toRemove.add(this);
	}
	
	public void updateSubject(Subject observed) {
		
	}
	
	public void detachSubject(Subject observed) {
		if (observed == destination) {
			this.destination = null;
		}
	}
	
	public void attachObserver(Observer observer) {
		this.observers.add(observer);
	}
	
	public void detachObserver(Observer observer) {
		this.observers.remove(observer);
		observer.detachSubject(this);
	}

	public void notifyObservers() {
		for (int i = observers.size() - 1; i >= 0; i--) {
			Observer observer = observers.get(i);
			observer.updateSubject(this);
		}
	}
	
	public void removeOther(GameObject object) {
		if (object == destination) {
			this.destination = null;
		}
		observers.remove(object);
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
	
	//returns 0 for no collision, 1 for dx = 0, 2 for dy = 0, and 3 for both = 0
	public int detectCollision(GameObject object, double dx, double dy) {
		double newX = this.absoluteX + dx;
		double newY = this.absoluteY + dy;
		int collisionKey = 0;
	
		if (object != this) {
			if (dx > 0) {
				if (object.contains(newX + objectWidth / 2, absoluteY + objectHeight / 2) ||
					object.contains(newX + objectWidth / 2, absoluteY - objectHeight / 2)) {
					collisionKey += 1;
				}
			} else if (dx < 0) {
				if (object.contains(newX - objectWidth / 2, absoluteY + objectHeight / 2) ||
					object.contains(newX - objectWidth / 2, absoluteY - objectHeight / 2)) {
					collisionKey += 1;
				}
			}
			
			if (dy > 0) {
				if (object.contains(absoluteX + objectWidth / 2, newY + this.objectHeight / 2) ||
					object.contains(absoluteX - objectWidth / 2, newY + this.objectHeight / 2)) {
					collisionKey = collisionKey == 1 ? 3 : 2;
				}
			} else if (dy < 0) {
				if (object.contains(absoluteX + objectWidth / 2, newY - this.objectHeight / 2) ||
					object.contains(absoluteX - objectWidth / 2, newY - this.objectHeight / 2)) {
					collisionKey = collisionKey == 1 ? 3 : 2;
				}
			}
			if (collisionKey == 0 && dx != 0 && dy != 0) {
				if (object.contains(newX + objectWidth / 2, newY + objectHeight / 2) ||
					object.contains(newX - objectWidth / 2, newY + objectHeight / 2) ||
					object.contains(newX + objectWidth / 2, newY - objectHeight / 2) ||
					object.contains(newX - objectWidth / 2, newY - objectHeight / 2)) {
					
					collisionKey = 3;
				}
			}
		}
		return collisionKey;
	}
	
	public double[] collide(GameObject object, int collisionKey, double dx, double dy) {
		double[] returnVal = { dx, dy };
		//If object is solid, adjust dx and dy appropriately so that you don't move through it
		if(object.solid) {
			switch (collisionKey) {
				case 1: returnVal[0] = 0;break;
				case 2: returnVal[1] = 0;break;
				case 3: returnVal[0] = 0; returnVal[1] = 0;break;
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
}
