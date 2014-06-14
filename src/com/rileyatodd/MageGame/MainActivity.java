package com.rileyatodd.MageGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    
    public void newGameScreen(View view) {
    	Intent intent = new Intent(this, NewGameScreen.class);
    	startActivity(intent);
    }
    
    public void signIn(View view) {
    	Intent intent = new Intent(this, SignIn.class);
    	startActivity(intent);
    }
    
    public void settings(View view) {
    	Intent intent = new Intent(this, Settings.class);
    	startActivity(intent);
    }
    
    public void startGame(View view) {
    	Intent intent = new Intent(this, GameActivity.class);
    	startActivity(intent);
    }
}
