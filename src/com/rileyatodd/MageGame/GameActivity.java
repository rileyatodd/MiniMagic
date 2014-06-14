package com.rileyatodd.MageGame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

	private final static String TAG = GameActivity.class.getSimpleName();
	private GameInstance gameInstance;
	public GameView gameView;
	private RenderThread renderThread;
	public Resources resources;
	public CharacterCard targetFrame;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//request to turn off the title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//make it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				             WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//get the resources
		this.resources = this.getResources();
		
		//Create gameView, gameThread, and gameInstance, and associate them with each other
		
		gameView = new GameView(this);	
		renderThread = new RenderThread();
		gameInstance = new GameInstance(this, gameView.width, gameView.height);
		gameView.gameInstance = gameInstance;
		gameView.renderThread = renderThread;
		gameView.gameActivity = this;
		gameInstance.gameActivity = this;
		gameView.loadUI();
		renderThread.gameActivity = this;
		renderThread.gameInstance = gameInstance;
		renderThread.gameView = gameView;
		renderThread.holder = gameView.getHolder();
		
		
		//Set our gameView as the main view
		setContentView(gameView);
		Log.d(TAG, "View added");
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}
}
