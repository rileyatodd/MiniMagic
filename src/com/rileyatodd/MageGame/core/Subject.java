package com.rileyatodd.MageGame.core;


public interface Subject {
	public void attachObserver(Observer observer);
	public void detachObserver(Observer observer);
	public void notifyObservers(String message);
}
