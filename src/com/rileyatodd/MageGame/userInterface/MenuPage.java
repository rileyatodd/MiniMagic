package com.rileyatodd.MageGame.userInterface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class MenuPage extends UIFrame {
	
	Button onButton;
	Button offButton;
	String name;
	boolean active;
	
	public MenuPage(Rect bounds, String name) {
		super(bounds);
		this.name = name;
		
		Paint onButtonPaint = new Paint();
		onButton = new Button(new Rect(0,0,0,0), name + " onButton", onButtonPaint);
		
		Paint offButtonPaint = new Paint();
		offButtonPaint.setColor(Color.WHITE);
		offButton = new Button(new Rect(bounds.right - 30, bounds.top, bounds.right, bounds.top+30), name+" offButton", offButtonPaint);
	}
	
	public void draw(Canvas canvas) {
		if (active) {
			for (UIFrame frame: this.getChildFrames()) {
				frame.draw(canvas);
			}
		}
		offButton.draw(canvas);
	}
	
}
