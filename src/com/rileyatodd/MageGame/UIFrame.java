package com.rileyatodd.MageGame;

import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public class UIFrame implements Observer {
	public Rect bounds;
	public ArrayList<UIFrame> childFrames = new ArrayList<UIFrame>();
	
	
	public UIFrame(Rect bounds) {
		this.bounds = new Rect(bounds.left, bounds.top, bounds.right, bounds.bottom);
	}
	
	
	public void draw(Canvas canvas) {
		for (UIFrame frame : childFrames) {
			frame.draw(canvas);
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		boolean retVal = false;
		for (UIFrame frame : childFrames) {
			if (frame.onTouchEvent(event)) {
				retVal = true;
			}
		}
		return retVal;
	}
	
	public void addChildFrame(UIFrame frame) {
		childFrames.add(frame);
	}
	
	public void getChildFrame(UIFrame frame) {
		childFrames.get(childFrames.indexOf(frame));
	}
	
	public void getChildFrame(int x) {
		childFrames.get(x);
	}
	

	public void detachSubject(Subject observed) {
		for (UIFrame child : childFrames) {
			child.detachSubject(observed);
		}
	}

	public void updateSubject(Subject observed) {
		// TODO Auto-generated method stub
		
	}

}
