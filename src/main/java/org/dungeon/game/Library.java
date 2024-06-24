package main.java.org.dungeon.game;

import main.java.org.dungeon.io.DLogger;

public abstract class Library {
	
	private boolean initialized = false;
	
	protected final boolean isInitialized() {
		return initialized;
	}
	
	protected final void initialize() {
		if (initialized) {
			DLogger.warning("Tried to initialize an already initialized Library class.");
		} else {
			load();
			initialized = true;
		}
	}
	
	abstract void load();
	
}
