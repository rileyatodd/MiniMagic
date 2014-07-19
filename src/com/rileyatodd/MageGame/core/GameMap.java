package com.rileyatodd.MageGame.core;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rileyatodd.MageGame.R;

public class GameMap {
	//Background tiles
	Bitmap[] tiles;
	int[][] tileMap;
	public int tileSizeX = 1024;
	public int tileSizeY = 1024;
	
	GameInstance gameInstance;
	
	//GameObjects that are part of the map / need to spawn
	ArrayList<GameObject> objects;
	
	
	
	GameMap(GameInstance gameInstance) {
		this.gameInstance = gameInstance;
		tiles = new Bitmap[1];
		tileMap = new int[30][30];
		tiles[0] = BitmapFactory.decodeResource(gameInstance.gameActivity.getResources(), R.drawable.grid);
		for (int i = 0; i < tileMap.length; i++) {
			for (int j = 0; j < tileMap[0].length; j ++) {
				tileMap[i][j] = 0;
			}
		}
	}
	
}
