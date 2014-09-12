package com.rileyatodd.MageGame.userInterface;

import java.util.ArrayList;

import android.graphics.Rect;
import android.util.Log;

public class InGameMenu extends UIFrame implements ButtonCallback {

	private ArrayList<MenuPage> menuPages;
	private MenuPage activePage;
	private boolean open;
	private Button openCloseButton;
	
	public InGameMenu(Rect bounds) {
		super(bounds);
		//create open and close buttons and add this as their callbacks
		openCloseButton = new Button(new Rect(bounds.right - 30, bounds.top, bounds.right, bounds.top + 30));
		openCloseButton.setText("X");
		openCloseButton.addButtonCallback(this);
		addChildFrame(openCloseButton);
		activePage = null;
		open = false;
		menuPages = new ArrayList<MenuPage>();
	}
	
	public void addMenuPage(MenuPage page) {
		page.onButton.addButtonCallback(this);
		page.offButton.addButtonCallback(this);
		menuPages.add(page);
		organizeButtons();
	}
	
	public void removeMenuPage(MenuPage page) {
		menuPages.remove(page);
		organizeButtons();
	}
	
	//resizes bounds of the onButtons for each menuPage so that they are all stacked vertically
	public void organizeButtons() {
		int buttonHeight = (this.getBounds().height() -100)/ menuPages.size();
		Rect bounds = new Rect(getBounds().left + 50, getBounds().top + 50, getBounds().right - 50, getBounds().top + 50 + buttonHeight);
		for (MenuPage menuPage: menuPages) {
			menuPage.onButton.setBounds(bounds);
			bounds.set(bounds.left, bounds.top + buttonHeight, bounds.right, bounds.bottom + buttonHeight);
		}
	}
	
	public void onButtonPress(Button button) {
		for (MenuPage page: menuPages) {
			if (button == page.onButton) {
				setActivePage(page);
				Log.d("InGameMenu", page.name + " selected");
			} else if (button == page.offButton) {
				setActivePage(null);
				Log.d("InGameMenu", "Off button pressed");
			}
		}
		if (button == openCloseButton) {
			if (open) {
				close();
				Log.d("InGameMenu", "Menu closed");
			} else {
				open();
				Log.d("InGameMenu", "Menu opened");
			}
		}
	}
	
	public void setActivePage(MenuPage page) {
		if (page == null) {
			if (activePage != null) {
				removeChildFrame(activePage);
				for (MenuPage eachPage: menuPages) {
					addChildFrame(eachPage.onButton);
				}
			}
		} else {
			if (activePage == null) {
				for (MenuPage eachPage: menuPages) {
					removeChildFrame(eachPage.onButton);
				}
			} else {
				removeChildFrame(activePage);
			}
			addChildFrame(page);
		}
		activePage = page;
		if (activePage != null) {
			Log.d("InGameMenu", "active page: "+ activePage.name);
		} else {
			Log.d("InGameMenu", "active page: null");
		}
	}
	
	public void open() {
		//Add child frames
		for (MenuPage page : menuPages) {
			addChildFrame(page.onButton);
		}
		open = true;
	}
	
	//closing the menu while a page is active results in weird behavior so currently im inactivating it on close
	public void close() {
		//deactivate active page
		setActivePage(null);
		
		//Remove child frames
		for (UIFrame frame : this.getChildFrames()) {
			removeChildFrame(frame);
		}
		addChildFrame(openCloseButton);
		open = false;
	}

}
