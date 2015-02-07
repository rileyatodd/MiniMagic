package com.rileyatodd.MageGame.core;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
	int GAME_UPDATES_PER_SECOND = 25;
	int FRAME_PERIOD= 1000/GAME_UPDATES_PER_SECOND;
	int MAX_FRAME_SKIP = 10;
	
	private SurfaceHolder holder;
	private GameView gameView;
	
	private boolean running;
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public MainThread(SurfaceHolder holder, GameView gameView) {
		this.holder = holder;
		this.gameView = gameView;
	}
		
	@Override
	public void run() {
		Log.d(this.getClass().getName(), "Starting Game Loop");
		Canvas canvas;
		
		long beginTime;
		long timeDiff;
		int sleepTime;
		int framesSkipped;
		
		sleepTime = 0;
		
		while(running) {
			canvas = null;
			//Try to get a lock on the canvas from the surfaceHolder
			try {
				canvas = this.holder.lockCanvas();
				synchronized(holder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;
					this.gameView.update();
					this.gameView.render(canvas);
					timeDiff = System.currentTimeMillis() - beginTime;
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					//Sleep if you have time
					if (sleepTime > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							
						}
					}
					//We are behind so update without rendering
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIP) {
						this.gameView.update();
						//Add Frame Period to check if we've made it to the next frame
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {
				if (canvas != null) {
					this.holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
