package com.rileyatodd.MageGame.core;


import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;



public class RenderThread extends Thread {
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
		Log.d(TAG, "Starting Game Loop");
		while (isRunning) {
			//Attempt to lock canvas
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();

				//Update Game State
				gameInstance.updateState();
				
				//Draw the view and UI elements
				gameView.onDraw(canvas);
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	
	public void setRunning(Boolean bool) {
		this.isRunning = bool;
	}
}



