package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.Rift;
import com.rileyatodd.MageGame.userInterface.CharacterCard;

//Acts as container for all GameObjects, handles updating the game state by looping through all
//GameObjects and calling their update() function
//The GameInstance is also responsible for collision detection (Although this seemed to make it worse)
//Also handles drawing by calling onDraw() on all GameObjects however it draws the map itself
//------Consider refactoring to have the GameMap draw itself via  --------------
//------an onDraw() method that  GameInstance calls during draw  ---------------
public class GameInstance {
	public GameActivity gameActivity;
	public GameMap gameMap;
	public Player player1;
	public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	private ArrayList<GameObject> toAdd = new ArrayList<GameObject>();
	private ArrayList<GameObject> toRemove = new ArrayList<GameObject>();
	public CharacterCard targetFrame;
	public int viewCoordX;
	public int viewCoordY;
	public int width;
	public int height;
	
	@SuppressLint("NewApi")
	public GameInstance(GameActivity gameActivity, int width, int height) {
		this.gameActivity = gameActivity;
		this.width = width;
		this.height = height;
		this.player1 = new Player(1, null, width / 2, height / 2, this, "p1");
		this.gameObjects.add(player1);
		Bitmap block = BitmapFactory.decodeResource(gameActivity.resources, R.drawable.dice);
		Rift boss = new Rift(1, new BitmapDrawable(block), player1.shape.getCenter().x + 150, player1.shape.getCenter().y + 150, this, "block");
		boss.targetable=true;
		boss.setTarget(player1);
		this.gameObjects.add(boss);
		this.gameMap = new GameMap(this);
		this.gameMap.gameInstance = this;
		
	}
	
	public void removeObject(GameObject object) {
		this.toRemove.add(object);
	}
	
	public void addObject(GameObject object) {
		this.toAdd.add(object);
	}
	
	//updates game state
	public void updateState() {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject object = gameObjects.get(i);
			object.update();
		}
		for (int i = 0; i < toAdd.size(); i++) {
			GameObject object = toAdd.get(i);
			gameObjects.add(object);
		}
		toAdd = new ArrayList<GameObject>();

		for (int i = 0; i < toRemove.size(); i++) {
			GameObject object = toRemove.get(i);
			gameObjects.remove(object);	
		}
		toRemove = new ArrayList<GameObject>();
		
	}
	
	public void loadMap(GameMap map) {
		for (GameObject object: map.objects) {
			toAdd.add(object);  
		}
	}
	
	//Draw methods
	//GameObjects have their onDraw() method called
	//The GameMap is drawn by the GameInstance itself
	public void onDraw(Canvas canvas) {
		drawMap(canvas, viewCoordX, viewCoordY);
		drawObjects(canvas, viewCoordX, viewCoordY);
	}
	
	public void drawObjects(Canvas canvas, int viewCoordX, int viewCoordY) {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject object = gameObjects.get(i);
			object.draw(canvas, viewCoordX, viewCoordY);
		}
	}
	
	public void drawMap(Canvas canvas, int screenCoordX, int screenCoordY) {
		int x = player1.shape.getCenter().x / gameMap.tileSizeX;
		int y = player1.shape.getCenter().y / gameMap.tileSizeY;
		
		if (x > 0) {
			if (x == gameMap.tileMap.length-1) {
				x -= 2;
			}
			x -= 1;
		}
		if (y > 0) {
			if (y == gameMap.tileMap[0].length-1) {
				y -= 2;
			}
			y -= 1;
		}

		for (int i = x; i < x + 3; i++) {
			for (int j = y; j < y + 3; j++) {
				canvas.drawBitmap(gameMap.tiles[gameMap.tileMap[i][j]], 
						i * gameMap.tileSizeX + x % gameMap.tileSizeX - viewCoordX,
						j * gameMap.tileSizeY + y % gameMap.tileSizeY - viewCoordY, null);
			}
		}
	}
	
	
	
	//Collision detection
	//returns 0 for no collision, 1 for dx = 0, 2 for dy = 0, and 3 for both = 0
	public ArrayList<boolean[]> detectCollisions(GameObject a, double dx, double dy) {
		ArrayList<boolean[]> collisions = new ArrayList<boolean[]>();
		for (GameObject b: gameObjects) {
			if (b != a) {
				boolean[] collision = new boolean[2];
				if (a.shape instanceof Circle && b.shape instanceof Circle) {
					Circle aShape = (Circle) a.shape;
					Circle bShape = (Circle) b.shape;
					if (b != a) {
						double xDiffNorm = aShape.getCenter().x - bShape.getCenter().x;
						double yDiffNorm = aShape.getCenter().y - bShape.getCenter().y;
						double xDiffShifted = aShape.getCenter().x + dx - bShape.getCenter().x;
						double yDiffShifted = aShape.getCenter().y + dy - bShape.getCenter().y;
						double radiusSum = aShape.radius + bShape.radius;
						
						//Check if xShifted circle collides
						if (radiusSum*radiusSum >= (xDiffShifted*xDiffShifted + yDiffNorm*yDiffNorm))	{
							collision[0] = true;
						}
						//Check if yShifted rectangle collides
						if (radiusSum*radiusSum >= (xDiffNorm*xDiffNorm + yDiffShifted*yDiffShifted)) {
							collision[1] = true;
						}
					}
				} else {
					Rectangle aShape = null;
					Rectangle bShape = null;
					if (a.shape instanceof Circle) {
						Circle aCircle = (Circle)a.shape;
						aShape = new Rectangle(aCircle.getCenter().x, aCircle.getCenter().y, aCircle.radius, aCircle.radius*2);
						bShape = (Rectangle)b.shape;
					} else if (b.shape instanceof Circle) {
						Circle bCircle = (Circle)b.shape;
						bShape = new Rectangle(bCircle.getCenter().x, bCircle.getCenter().y, bCircle.radius, bCircle.radius*2);
						aShape = (Rectangle)a.shape;
					} else {
						aShape = (Rectangle)a.shape;
						bShape = (Rectangle)b.shape;
					}
					
					double aRight = aShape.getRight();
					double aLeft = aShape.getLeft();
					double aTop = aShape.getTop();
					double aBottom = aShape.getBottom();
					
					double bRight = bShape.getRight();
					double bLeft = bShape.getLeft();
					double bTop = bShape.getTop();
					double bBottom = bShape.getBottom();
					if (b != a) {
						//Check if xShifted rectangle collides
						if (!(aRight + dx <= bLeft || aLeft + dx >= bRight
								|| aTop >= bBottom || aBottom <= bTop)) {
							collision[0] = true;
						}
						//Check if yShifted rectangle collides
						if (!(aRight <= bLeft || aLeft >= bRight
								|| aTop + dy >= bBottom || aBottom + dy <= bTop)) {
							collision[1] = true;
						}
					}
				}
				if (collision[0] || collision[1]) {
					a.onCollision(b);
					b.onCollision(a);
					if (b.solid && a.solid) {
						collisions.add(collision);
					}
				}
			}
		}
		return collisions;
	}
	
}
