package com.rileyatodd.MageGame.userInterface;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.rileyatodd.MageGame.core.Observer;
import com.rileyatodd.MageGame.core.Subject;

public class UIFrame implements Observer {
	private Rect bounds;
	private CopyOnWriteArrayList<UIFrame> childFrames = new CopyOnWriteArrayList<UIFrame>();
	
	
	public UIFrame(Rect bounds) {
		this.setBounds(new Rect(bounds.left, bounds.top, bounds.right, bounds.bottom));
	}
	
	
	public void draw(Canvas canvas) {
		for (UIFrame frame : getChildFrames()) {
			frame.draw(canvas);
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		boolean retVal = false;
		for (UIFrame frame : getChildFrames()) {
			if (frame.onTouchEvent(event)) {
				retVal = true;
			}
		}
		return retVal;
	}
	
	public void addChildFrame(UIFrame frame) {
		childFrames.add(frame);
	}
	
	public void removeChildFrame(UIFrame frame) {
		childFrames.remove(frame);
	}
	
	public void getChildFrame(UIFrame frame) {
		childFrames.get(getChildFrames().indexOf(frame));
	}
	
	public void getChildFrame(int x) {
		childFrames.get(x);
	}

	public void updateSubject(Subject observed, String message) {

	}


	public Rect getBounds() {
		return bounds;
	}


	public void setBounds(Rect bounds) {
		this.bounds = new Rect(bounds);
	}


	public CopyOnWriteArrayList<UIFrame> getChildFrames() {
		return childFrames;
	}


	public void setChildFrames(CopyOnWriteArrayList<UIFrame> childFrames) {
		this.childFrames = childFrames;
	}

}
