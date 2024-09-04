package org.dungeon.io;

public final class Sleeper {
	
	private Sleeper() {
	}
	
	public static void sleep(int milliseconds) {
		if (milliseconds > 0) {
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException logged) {
				DLogger.warning("Sleeper was interrupted!");
			}
		}
	}

}
