package com.rileyatodd.MageGame;

public interface Observer {
	public void updateSubject(Subject observed);
	public void detachSubject(Subject observed);
}
