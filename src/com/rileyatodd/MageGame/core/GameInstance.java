package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.rileyatodd.MageGame.R;
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
	public ArrayList<GameObject> toAdd = new ArrayList<GameObject>();
	public ArrayList<GameObject> toRemove = new ArrayList<GameObject>();
	public CharacterCard targetFrame;
	public int viewCoordX;
	public int viewCoordY;
	public int width;
	public int height;
	
	@SuppressLint("NewApi")
	public GameInstance(GameActivity gameActivity, int width, int height) {
		this.gameActivity = gameActivity;
		Bitmap p1 = BitmapFactory.decodeResource(gameActivity.resources, R.drawable.sprite);
		this.width = width;
		this.height = height;
		this.player1 = new Player(1, p1, width / 2, height / 2, this, "p1");
		this.gameObjects.add(player1);
		Bitmap block = BitmapFactory.decodeResource(gameActivity.resources, R.drawable.dice);
		Character boss = new Character(1, block, player1.absoluteX + 150, player1.absoluteY + 150, this, "block");
		boss.targetable=true;
		//boss.target = player1;
		this.gameObjects.add(boss);
		this.gameMap = new GameMap(this);
		this.gameMap.gameInstance = this;
		
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
		drawMap(canvas);
		drawObjects(canvas);
	}
	
	public void drawObjects(Canvas canvas) {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject object = gameObjects.get(i);
			object.draw(canvas);
		}
	}
	
	public void drawMap(Canvas canvas) {
		int x = player1.absoluteX / gameMap.tileSizeX;
		int y = player1.absoluteY / gameMap.tileSizeY;
		
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
		double aRight = a.absoluteX + a.objectWidth/2;
		double aLeft = a.absoluteX - a.objectWidth/2;
		double aTop = a.absoluteY - a.objectHeight/2;
		double aBottom = a.absoluteY + a.objectHeight/2;
		
		for (GameObject b: gameObjects) {
			boolean[] collision = new boolean[2];
			double bRight = b.absoluteX + b.objectWidth/2;
			double bLeft = b.absoluteX - b.objectWidth/2;
			double bTop = b.absoluteY - b.objectHeight/2;
			double bBottom = b.absoluteY + b.objectHeight/2;
			
			if (b != a) {
				//Check if xShifted rectangle collides
				if (!(aRight + dx <= bLeft || aLeft + dx >= bRight || aTop >= bBottom || aBottom <= bTop)) {
					collision[0] = true;
				}
				//Check if yShifted rectangle collides
				if (!(aRight <= bLeft || aLeft >= bRight || aTop + dy >= bBottom || aBottom + dy <= bTop)) {
					collision[1] = true;
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
		return collisions;
	}
	
}
