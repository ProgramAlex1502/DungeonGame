package org.dungeon.util;

public class StopWatch {
	
	private static final int MILLISECONDS_IN_NANOSECONDS = 1000000;
	private final long time;
	
	public StopWatch() {
		time = System.nanoTime();
	}
	
	public String toString() {
		final long difference = System.nanoTime() - time;
		return String.format("%d ms", difference / MILLISECONDS_IN_NANOSECONDS);
	}

}
