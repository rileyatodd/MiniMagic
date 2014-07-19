package com.rileyatodd.MageGame.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.rileyatodd.MageGame.Destination;
import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.userInterface.Button;
import com.rileyatodd.MageGame.userInterface.CharacterCard;
import com.rileyatodd.MageGame.userInterface.UIFrame;
//View for the main game screen
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	public RenderThread renderThread;
	public GameInstance gameInstance;
	public GameActivity gameActivity;
	public int width;
	public int height;
	//Coords of top-left corner of view relative to gameInstance
	public int viewCoordX;
	public int viewCoordY;
	private final static String TAG = GameView.class.getSimpleName();
	public UIFrame uiFrame;
	public Spell selectedSpell = null;
	
	@SuppressLint("NewApi")
	public GameView(Context c, AttributeSet attrs) {
		super(c, attrs);
		TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.GameView);
		
		gameActivity = (GameActivity) c;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
		Display display = gameActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		this.width = size.x;
		this.height = size.y;
		
		a.recycle();
	}
	
	@SuppressLint("NewApi")
	public GameView(Context context) {
		super(context);
		gameActivity = (GameActivity) context;
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
		Display display = gameActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		this.width = size.x;
		this.height = size.y;
	}
	
	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int width = this.getWidth();
		int height = this.getHeight();
		Log.d(TAG, "" + width + ", " + height);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		renderThread.setRunning(true);
		renderThread.start();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry) {
			try {
				renderThread.join();
				retry = false;
			} catch (InterruptedException e){
				//Try again to shut down the gameThread
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			boolean targetedSomething = false;
			//If the click is on a UI element let it handle it
			if (uiFrame.onTouchEvent(event)) {
				return true;
			}
			if (selectedSpell != null) {
				gameInstance.player1.castSpell(selectedSpell, (int)x, (int)y);
				Log.d("GameView", "Should have just casted: " + selectedSpell);
				return true;
			}
			// Otherwise check if you are targeting something
			for (int i = 0; i < gameInstance.gameObjects.size(); i++) {
				GameObject object = gameInstance.gameObjects.get(i);
				if (object.targetable && object.contains(x + gameInstance.viewCoordX, y + gameInstance.viewCoordY)) {
					gameInstance.player1.setTarget(object);
					Log.d(TAG, "Object selected");
					targetedSomething = true;
				} 
			}
			//And if you're just clicking on empty space, make a destination
			if (!targetedSomething) {
				//Remove previous player destination if there is one
				if (gameInstance.player1.destination != null){
					gameInstance.player1.destination.despawn();
				}
				//Create new player destination and add the player as an observer of it
				GameObject playerDestination = new Destination(gameInstance.player1, 
						   (int)(x + gameInstance.viewCoordX),
						   (int)(y + gameInstance.viewCoordY),
						   this.gameInstance, "crosshairs");
				playerDestination.attachObserver(gameInstance.player1);
				gameInstance.player1.destination = playerDestination;
				gameInstance.toAdd.add(playerDestination);		
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	@SuppressLint("WrongCall")
	public void onDraw(Canvas canvas) {
		canvas.drawColor(android.graphics.Color.BLACK);
		gameInstance.onDraw(canvas);
		uiFrame.draw(canvas);
	}
	
	public void loadUI() {
		//Create outer uiFrame
		Rect bounds = new Rect(0, 0, width, height);
		this.uiFrame = new UIFrame(bounds);
		
		//Create targetFrame, connect to player1 and add to outer frame
		bounds.set(bounds.left, bounds.top, bounds.right, bounds.top + 50);
		CharacterCard targetFrame = new CharacterCard(bounds);
		gameInstance.player1.attachObserver(targetFrame);
		targetFrame.targetOf = gameInstance.player1;
		uiFrame.addChildFrame(targetFrame);
		
		//Create playerFrame, connect to player1, and add to outer frame
		bounds.set(bounds.left, height - 50 , bounds.right, height);
		CharacterCard playerFrame = new CharacterCard(bounds);
		playerFrame.setCharacter(gameInstance.player1);
		gameInstance.player1.attachObserver(playerFrame);
		uiFrame.addChildFrame(playerFrame);
		
		//Create spellFrame
		bounds.set(bounds.left, height - 150, bounds.right, height - 50);
		UIFrame spellFrame = new UIFrame(bounds);
		for (int i = 0; i < gameInstance.player1.activeSpells.size(); i++) {
			Spell spell = gameInstance.player1.activeSpells.get(i);
			bounds.set(bounds.left + i * width / 6, height - 150, bounds.left + (i + 1) * width / 6, height - 50);
			Button button = new Button(bounds);
			button.addButtonCallback(spell);
			button.paint.setColor(spell.paint.getColor());
			spellFrame.addChildFrame(button);
			Log.d("GameView", "Added " + spell.name + " to spellFrame");
		}
		uiFrame.addChildFrame(spellFrame);
	}
}
