package com.rileyatodd.MageGame.core;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.rileyatodd.MageGame.R;

public class NewGameScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game_screen, menu);
		return true;
	}

}
