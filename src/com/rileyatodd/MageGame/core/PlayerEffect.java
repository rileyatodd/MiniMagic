package com.rileyatodd.MageGame.core;


public class PlayerEffect {
	public double duration;
	public double startTime;
	public double endTime;
	public boolean on;
	public Character target;
	public String name; 
	
	
	public PlayerEffect(double duration, Character target, String name) {
		this.duration = duration;
		this.target = target;
		this.target.activeEffects.add(this);
		this.name = name;
		//End time of 0 indicates the effect hasn't been activated yet
		endTime = 0;
	}
	
	public void activate() {
		startTime = System.currentTimeMillis();
		endTime = startTime + duration*1000;
		target.speed *= 3;
	}
	
	public void deactivate() {
		target.speed /= 3;
		this.target.activeEffects.remove(this);
	}
}
