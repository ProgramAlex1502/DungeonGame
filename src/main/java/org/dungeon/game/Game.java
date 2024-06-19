package main.java.org.dungeon.game;

import main.java.org.dungeon.gui.GameWindow;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.Loader;

public class Game {
	
	//TODO: finish Game class
	
	private static GameWindow gameWindow;
	private static GameState gameState;
	
	public static GameWindow getGameWindow() {
		return gameWindow;
	}
	
	public static GameState getGameState() {
		return gameState;
	}
	
	public static void exit() {
		if (!gameState.isSaved()) {
			Loader.saveGame(gameState);
		}
		
		DLogger.info("Exited with no problems.");
		System.exit(0);
	}

}
