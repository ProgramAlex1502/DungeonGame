package org.dungeon.io;

public final class Sleeper {
	
	private Sleeper() {
		throw new AssertionError();
	}
	
	public static void sleep(long milliseconds) {
		if (milliseconds > 0) {
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException logged) {
				DLogger.warning("Sleeper was interrupted!");
			}
		}
	}

}
