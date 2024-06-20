package main.java.org.dungeon.game;

import main.java.org.dungeon.gui.GameWindow;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.io.Loader;

public class Game {
	
	//TODO: finish Game class
	
	private static int turnLength = 0;
	private static boolean configurationsChanged = false;
	
	private static GameWindow gameWindow;
	private static GameState gameState;
	
	public static GameWindow getGameWindow() {
		return gameWindow;
	}
	
	public static GameState getGameState() {
		return gameState;
	}
	
	public static void renderTurn(IssuedCommand issuedCommand) {
		getGameWindow().clearTextPane();
		processInput(issuedCommand);
		
		if (gameState.getHero().isDead()) {
			IO.writeString("You died.");
			gameState = Loader.loadGame(null);
		} else {
			gameState.getWorld().rollDate(turnLength);
			Engine.refresh();
			
			if (turnLength != 0 || configurationsChanged) {
				gameState.setSaved(false);
			}
		}
	}
	
	private static void processInput(IssuedCommand issuedCommand) {
		// TODO: complete "game.processInput(IssuedCommand issuedCommand)" method
	}
	
	public static void exit() {
		if (!gameState.isSaved()) {
			Loader.saveGame(gameState);
		}
		
		DLogger.info("Exited with no problems.");
		System.exit(0);
	}

}
