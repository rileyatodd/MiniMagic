package com.rileyatodd.MageGame.userInterface;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class InGameMenu extends UIFrame implements ButtonCallback {

	private Paint menuPaint;
	private Paint buttonPaint;
	private ArrayList<Button> menuButtons;
	private int activePanel;
	private boolean open;
	
	public InGameMenu(Rect bounds) {
		super(bounds);
		menuPaint = new Paint();
		buttonPaint = new Paint();
		menuButtons = new ArrayList<Button>();
		Button exitButton = new Button(new Rect(bounds.right - 10, bounds.top, bounds.left, bounds.top + 10));
		menuButtons.add(exitButton);
		activePanel = -1;
		open = false;
	}
	
	public void addMenuOption(String name, UIFrame frame) {
		
	}
	
	public void onButtonPress(Button button) {
		//Change activepanel appropriately....
		
	}
	
	public void draw(Canvas canvas) {
		for (Button button: menuButtons) {
			button.draw(canvas);
		}
	}

}
