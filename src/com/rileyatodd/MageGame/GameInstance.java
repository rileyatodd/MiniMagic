package com.rileyatodd.MageGame;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class GameInstance {
	//private final static String TAG = GameInstance.class.getSimpleName();
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
		gameObjects.add(player1);
		Bitmap block = BitmapFactory.decodeResource(gameActivity.resources, R.drawable.dice);
		Character boss = new Character(1, block, player1.absoluteX + 150, player1.absoluteY + 150, this, "block");
		boss.targetable=true;
		//boss.target = player1;
		gameObjects.add(boss);
		gameMap = new GameMap(this);
		gameMap.gameInstance = this;
		
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
			//Log.d(TAG, "Added : " + object.toString());
			//Log.d(TAG, gameObjects.toString());
		}
		toAdd = new ArrayList<GameObject>();

		for (int i = 0; i < toRemove.size(); i++) {
			GameObject object = toRemove.get(i);
			gameObjects.remove(object);	
			//Log.d(TAG, "Removed : " + object.toString());
			//Log.d(TAG, gameObjects.toString());
		}
		toRemove = new ArrayList<GameObject>();
		
	}
	
	public void loadMap(GameMap map) {
		for (GameObject object: map.objects) {
			toAdd.add(object);  
		}
	}
	
	
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
	
}
