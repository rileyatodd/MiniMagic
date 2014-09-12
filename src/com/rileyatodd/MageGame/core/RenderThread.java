package com.rileyatodd.MageGame.core;


import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;



public class RenderThread extends Thread {
	int GAME_UPDATES_PER_SECOND = 25;
	int MILLIS_PER_FRAME= 1000/GAME_UPDATES_PER_SECOND;
	int MAX_FRAME_SKIP = 10;
	
	SurfaceHolder holder;
	public GameView gameView;
	public GameInstance gameInstance;
	public GameActivity gameActivity;
	private Boolean isRunning;
	private static final String TAG = RenderThread.class.getSimpleName();
	
	public RenderThread() {
		super();
	}
	
	@SuppressLint("WrongCall")
	@Override
	public void run() {
		long startTime = 0;
		long elapsedTime = 0;
		long longestUpdateTime = 0;
		long longestDrawTime = 0;
		
		Log.d(TAG, "Starting Game Loop");
		
		Canvas canvas = null;
		long nextGameUpdate = System.currentTimeMillis();
		
		while (isRunning) {
			int skippedFrames = 0;
			while(System.currentTimeMillis() > nextGameUpdate && skippedFrames < MAX_FRAME_SKIP) {
				
				startTime = System.currentTimeMillis();
				gameInstance.updateState();
				elapsedTime = System.currentTimeMillis() - startTime;
				if (elapsedTime > longestUpdateTime) {
					longestUpdateTime = elapsedTime;
					Log.d(this.getClass().getSimpleName(), "whole game updated in " + longestUpdateTime/1000.0 + " seconds");
				}
				
				nextGameUpdate += MILLIS_PER_FRAME;
				skippedFrames ++;
			}
			

			startTime = System.currentTimeMillis();
			//Attempt to lock canvas
			try {
				canvas = holder.lockCanvas();
				Log.d("RenderThread", "" + canvas.isHardwareAccelerated());
				
				//Draw the view and UI elements
				gameView.onDraw(canvas);
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
			elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > longestDrawTime) {
				longestDrawTime = elapsedTime;
				Log.d(this.getClass().getSimpleName(), "everything drawn in " + longestDrawTime/1000.0 + " seconds");
			}
		}
	}
	
	public void setRunning(Boolean bool) {
		this.isRunning = bool;
	}
}



