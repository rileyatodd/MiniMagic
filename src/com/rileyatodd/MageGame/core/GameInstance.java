package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.spells.SummonMinion;
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
	private Point viewLoc;
	public int width;
	public int height;
	private Spell selectedSpell = null;
	
	@SuppressLint("NewApi")
	public GameInstance(GameActivity activity, int width, int height) {
		this.gameActivity = activity;
		this.width = width;
		this.height = height;
		this.player1 = new Player(1, this, null, new Point(width / 2, height / 2), "p1");
		this.gameObjects.add(player1);
		Bitmap block = BitmapFactory.decodeResource(gameActivity.resources, R.drawable.dice);
		Bitmap spider = BitmapFactory.decodeResource(gameActivity.resources, R.drawable.spider);
		Character boss = new Character(1, this, new BitmapDrawable(block), new Point(player1.shape.getCenter().x + 150, player1.shape.getCenter().y + 150), "block");
		Character minion = new Character(1, this, new BitmapDrawable(spider), boss.shape.getCenter(), "boss minion");
		boss.setAutoAttack(new SummonMinion(minion, this));
		boss.targetable=true;
		boss.setTarget(player1);
		this.gameObjects.add(boss);
		this.gameMap = new GameMap(this);
		this.gameMap.gameInstance = this;
		viewLoc = new Point(0,0);
		
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
	//GameObjects have their draw() method called
	//The GameMap is drawn by the GameInstance itself
	public void render(Canvas canvas) {
		drawMap(canvas);
		drawObjects(canvas);
	}
	
	public void drawObjects(Canvas canvas) {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject object = gameObjects.get(i);
			object.draw(canvas, viewLoc);
		}
	}
	
	public void drawMap(Canvas canvas) {
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
						i * gameMap.tileSizeX + x % gameMap.tileSizeX - viewLoc.x,
						j * gameMap.tileSizeY + y % gameMap.tileSizeY - viewLoc.y, null);
			}
		}
	}
	
	
	
	//Collision detection
	//returns a boolean array with array[0] being wether or not the collision involved x movement and array[1] for y movement
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
						aShape = new Rectangle(aCircle.getCenter(), aCircle.radius, aCircle.radius*2);
						bShape = (Rectangle)b.shape;
					} else if (b.shape instanceof Circle) {
						Circle bCircle = (Circle)b.shape;
						bShape = new Rectangle(bCircle.getCenter(), bCircle.radius, bCircle.radius*2);
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
					if (a.onCollision(b) && b.onCollision(a)) {
						Log.d("GameInstance", "Collision between " + b.getClass().getCanonicalName() + " and " + a.getClass().getCanonicalName());
						collisions.add(collision);
					}
				}
			}
		}
		return collisions;
	}

	public Point getViewLoc() {
		return viewLoc;
	}

	public void setViewLoc(Point viewLoc) {
		this.viewLoc = viewLoc;
	}

	public Spell getSelectedSpell() {
		return selectedSpell;
	}

	public void setSelectedSpell(Spell selectedSpell) {
		this.selectedSpell = selectedSpell;
	}
	
}
