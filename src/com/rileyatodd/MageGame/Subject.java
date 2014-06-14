package com.rileyatodd.MageGame;

public interface Subject {
	public void attachObserver(Observer observer);
	public void detachObserver(Observer observer);
	public void notifyObservers();
}
