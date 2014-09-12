package com.rileyatodd.MageGame.userInterface;

import android.graphics.Paint;
import android.graphics.Rect;

public class MenuPage extends UIFrame {
	
	Button onButton;
	Button offButton;
	String name;
	
	public MenuPage(Rect bounds, String name) {
		super(bounds);
		this.name = name;
		
		Paint onButtonPaint = new Paint();
		onButton = new Button(new Rect(0,0,0,0), name + " onButton", onButtonPaint);
		
		Paint offButtonPaint = new Paint();
		offButton = new Button(new Rect(bounds.right - 30, bounds.top, bounds.right, bounds.top+30),"X", offButtonPaint);
		addChildFrame(offButton);
	}
	
}
