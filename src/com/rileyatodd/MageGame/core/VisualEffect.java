package com.rileyatodd.MageGame.core;

public class VisualEffect extends GameObject {

	public VisualEffect(GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		super(gameInstance, drawable, loc, name);
		
	}
	
	public void update() {
		//If the animation is not running, remove from gameObjects	
		Animation animation = (Animation)this.getDrawable();
		if (!animation.running && animation.getRepNum() >= animation.getRepetitions()) {
			gameInstance.removeObject(this);
			animation.reset();
		} else {
			super.update();
		}
	}

}
