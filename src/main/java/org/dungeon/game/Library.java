package main.java.org.dungeon.game;

import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.util.StopWatch;

abstract class Library {
	
	private boolean initialized = false;
	
	final boolean isInitialized() {
		return initialized;
	}
	
	final void initialize() {
		if (initialized) {
			DLogger.warning("Tried to initialize an already initialized Library class.");
		} else {
			StopWatch stopWatch = new StopWatch();
			load();
			DLogger.info("Loading took " + stopWatch.toString() + ".");
			initialized = true;
		}
	}
	
	abstract void load();
	
}
