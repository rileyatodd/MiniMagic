package com.rileyatodd.MageGame.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.rileyatodd.MageGame.R;
import com.rileyatodd.MageGame.userInterface.Button;
import com.rileyatodd.MageGame.userInterface.CharacterCard;
import com.rileyatodd.MageGame.userInterface.InGameMenu;
import com.rileyatodd.MageGame.userInterface.MenuPage;
import com.rileyatodd.MageGame.userInterface.UIFrame;
//View for the main game screen
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private MainThread mainThread;
	private GameInstance gameInstance;
	private GameActivity gameActivity;
	public int width;
	public int height;
	private final static String TAG = GameView.class.getSimpleName();
	private UIFrame uiFrame;
	
	@SuppressLint("NewApi")
	public GameView(Context c, AttributeSet attrs) {
		super(c, attrs);
		TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.GameView);
		gameActivity = (GameActivity) c;
		
		//This class will intercept and handle events from SurfaceHolder.Callback
		getHolder().addCallback(this);
		setFocusable(true);
		
		//Find size of display in pixels
		Display display = gameActivity.getWindowManager().getDefaultDisplay();
		android.graphics.Point size = new android.graphics.Point();
		display.getSize(size);
		this.width = size.x;
		this.height = size.y;
		
		//Create the game instance
		gameInstance = new GameInstance(gameActivity, width, height);	
		Log.d("GameView", "gameInstance set");
		
		loadUI();
		
		//Create the main thread
		mainThread = new MainThread(getHolder(), this);
		
		a.recycle();
	}
	
	@SuppressLint("NewApi")
	public GameView(Context context) {
		super(context);
		
		gameActivity = (GameActivity) context;
		
		//This class will intercept and handle events from SurfaceHolder.Callback
		getHolder().addCallback(this);
		setFocusable(true);
		
		//Find size of display in pixels
		Display display = gameActivity.getWindowManager().getDefaultDisplay();
		android.graphics.Point size = new android.graphics.Point();
		display.getSize(size);
		this.width = size.x;
		this.height = size.y;
		
		//Create the game instance
		gameInstance = new GameInstance(gameActivity, width, height);	
		Log.d("GameView", "gameInstance set");
		
		loadUI();
		
		//Create the main thread
		mainThread = new MainThread(getHolder(), this);
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
		Log.d("GameView", "SurfaceCreated");
		mainThread.setRunning(true);
		mainThread.start();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("GameView", "SurfaceDestroyed");
		boolean retry = true;
		while (retry) {
			try {
				mainThread.join();
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
			if (gameInstance.getSelectedSpell() != null) {
				gameInstance.player1.castSpell(gameInstance.getSelectedSpell(), new Point((int)x + gameInstance.getViewLoc().x, (int)y + gameInstance.getViewLoc().y));
				return true;
			}
			// Otherwise check if you are targeting something
			for (int i = 0; i < gameInstance.gameObjects.size(); i++) {
				GameObject object = gameInstance.gameObjects.get(i);
				if (object.targetable && object.shape.contains(new Point((int)(x + gameInstance.getViewLoc().x), (int)(y + gameInstance.getViewLoc().y)))) {
					gameInstance.player1.setTarget(object);
					Log.d(TAG, "Object selected");
					targetedSomething = true;
				} 
			}
			//And if you're just clicking on empty space, make a destination
			if (!targetedSomething) {
				//Remove previous player destination if there is one
				if (gameInstance.player1.getDestination() != null){
					gameInstance.player1.getDestination().despawn();
				}
				//Create new player destination and add the player as an observer of it
				GameObject playerDestination = gameInstance.player1.createDestination( (int)(x + gameInstance.getViewLoc().x),
						   										(int)(y + gameInstance.getViewLoc().y),
						   										this.gameInstance, "crosshairs");
				playerDestination.attachObserver(gameInstance.player1);
				gameInstance.player1.setDestination(playerDestination);
				gameInstance.addObject(playerDestination);		
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	public void render(Canvas canvas) {
		canvas.drawColor(android.graphics.Color.BLACK);
		gameInstance.render(canvas);
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
		targetFrame.setTargetOf(gameInstance.player1);
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
		int numSpells = gameInstance.player1.activeSpells.size();
		int buttonWidth = width / numSpells;
		int left = 0;
		int right = buttonWidth;
		for (int i = 0; i < gameInstance.player1.activeSpells.size(); i++) {
			Spell spell = gameInstance.player1.activeSpells.get(i);
			bounds.set(left, height - 150, right, height - 50);
			left+=buttonWidth;
			right+=buttonWidth;
			Button button = new Button(bounds);
			button.addButtonCallback(spell);
			button.backgroundPaint.setColor(spell.getPaint().getColor());
			spellFrame.addChildFrame(button);
			Log.d("GameView", "Added " + spell.getClass().getCanonicalName() + " to spellFrame");
		}
		uiFrame.addChildFrame(spellFrame);
		
		//Create Menu Frame
		bounds.set(0,0,width,height);
		InGameMenu menuFrame = new InGameMenu(bounds);
		//Add Menu Pages
		menuFrame.addMenuPage(new MenuPage(new Rect(bounds.left + 50, bounds.top+50, bounds.right-50,bounds.bottom-50), "spell selection"));
		
		//Add to main UIFrame
		uiFrame.addChildFrame(menuFrame);
		
	}
	
	public void update() {
		this.gameInstance.updateState();
	}
}
