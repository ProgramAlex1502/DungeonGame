package main.java.org.dungeon.io;

final public class Sleeper {
	
	private Sleeper() {
	}
	
	public static void sleep(int milliseconds) {
		if (milliseconds > 0) {
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException logged) {
				DLogger.warning("Sleeper was interrupted!");
			}
		} else {
			DLogger.warning("Tried to sleep a nonpositive amount of milliseconds!");
		}
	}

}
