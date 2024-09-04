package org.dungeon.util;

public class StopWatch {
	
	private final long time;
	
	public StopWatch() {
		time = System.nanoTime();
	}
	
	public String toString() {
		final long difference = System.nanoTime() - time;
		return String.format("%d ms", difference / 1000000);
	}

}
