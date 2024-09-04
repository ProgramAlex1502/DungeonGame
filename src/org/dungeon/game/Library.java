package org.dungeon.game;

import org.dungeon.io.DLogger;
import org.dungeon.util.StopWatch;

abstract class Library {
	
	private boolean uninitialized = true;
	
	final boolean isUninitialized() {
		return uninitialized;
	}
	
	final void initialize() {
		if (uninitialized) {
			
			StopWatch stopWatch = new StopWatch();
			load();
			DLogger.info("Loading took " + stopWatch.toString() + ".");
			uninitialized = false;
		} else {
			DLogger.warning("Tried to initialize an already initialized Library class.");
		}
	}
	
	abstract void load();

}
