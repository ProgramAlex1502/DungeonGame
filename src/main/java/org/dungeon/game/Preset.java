package main.java.org.dungeon.game;

public abstract class Preset {
	
	private boolean finished;
	
	void lock() {
		finished = true;
	}
	
	boolean isLocked() {
		return finished;
	}

}
