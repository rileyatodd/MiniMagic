package com.rileyatodd.MageGame.core;


public interface Observer {
	public void updateSubject(Subject observed);
	public void detachSubject(Subject observed);
}
