package com.rileyatodd.MageGame.objects;


import com.rileyatodd.MageGame.core.Drawable;
import com.rileyatodd.MageGame.core.GameInstance;
import com.rileyatodd.MageGame.core.Subject;
import com.rileyatodd.MageGame.geometry.Point;

public class Projectile extends GameObject {

	private int damage;
	private GameObject creator;
	
	
	public Projectile(int damage, GameObject creator, GameObject target, GameInstance gameInstance, Drawable drawable, Point loc, String name) {
		super(gameInstance, drawable, loc, name);
		this.damage = damage;
		this.creator = creator;
		this.setDestination(target);
		target.attachObserver(this);
	}
	
	@Override
	public boolean onCollision(GameObject object){
		if (object == creator) {
			return false;
		}else if (object instanceof Character && object != creator) {
			Character character = (Character) object;
			character.damage(damage);
			this.despawn();
			return true;
		} else if (object instanceof Scenery) {
			this.despawn();
			return true;
		} else {
			return false;
		}
	}
	
	public void updateSubject(Subject sub, String message) {
		if (message.equals("despawn")) {
			if (sub == getDestination()) {
				despawn();
			}
		}
	}
}
