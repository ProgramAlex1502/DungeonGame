package main.java.org.dungeon.game;

abstract class Preset {
	
	private boolean finished;
	
	void lock() {
		finished = true;
	}
	
	boolean isLocked() {
		return finished;
	}

}
