package com.rileyatodd.MageGame.core;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.rileyatodd.MageGame.userInterface.CharacterCard;

public class GameActivity extends Activity {

	private final static String TAG = GameActivity.class.getSimpleName();
	public GameView gameView;
	public Resources resources;
	public CharacterCard targetFrame;
	
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
		
		//Create gameView and set it as the main view
		setContentView(new GameView(this));
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
