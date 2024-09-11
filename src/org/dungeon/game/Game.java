package org.dungeon.game;


import org.dungeon.commands.Command;
import org.dungeon.commands.CommandCollection;
import org.dungeon.commands.CommandResult;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.gui.GameWindow;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.util.Messenger;

public class Game {

	private static GameWindow gameWindow;
	private static GameState gameState;
	private static CommandResult lastCommandResult;
	
	public static void main(String[] args) {
		GameData.loadGameData();
		gameWindow = new GameWindow();
		setGameState(loadAGameStateOrCreateANewOne());
	}
	
	private static GameState loadAGameStateOrCreateANewOne() {
		GameState gameState = Loader.loadGame();
		if (gameState == null) {
			gameState = Loader.newGame();
			if (!Loader.checkForAnySave()) {
				suggestTutorial();
			}
		}
		return gameState;
	}
	
	private static void suggestTutorial() {
		IO.writeNewLine();
		IO.writeString("You may want to issue 'tutorial' to learn the basics.");
	}
	
	public static GameWindow getGameWindow() {
		return gameWindow;
	}
	
	public static GameState getGameState() {
		return gameState;
	}
	
	public static void setGameState(GameState state) {
		gameState = state;
		if (state == null) {
			DLogger.info("Set the GameState field in Game to null.");
		} else {
			DLogger.info("Set the GameState field in Game to a GameState.");
			Engine.refresh();
			IO.writeNewLine();
			gameState.getHero().look(null);
		}
	}
	
	public static void renderTurn(IssuedCommand issuedCommand) {
		getGameWindow().clearTextPane();
		processInput(issuedCommand);
		if (gameState.getHero().isDead()) {
			IO.writeString("You died.");
			setGameState(loadAGameStateOrCreateANewOne());
		} else {
			if (lastCommandResult != null) {
				if (lastCommandResult.getDuration() > 0) {
					gameState.getWorld().rollDate(lastCommandResult.getDuration());
				}
				
				if (lastCommandResult.evaluateIfGameStateChanged()) {
					gameState.setSaved(false);
				}
			}
			Engine.refresh();
		}
	}
	
	private static void processInput(IssuedCommand issuedCommand) {
        gameState.getCommandHistory().addCommand(issuedCommand);
        gameState.getStatistics().addCommand(issuedCommand);

        Command command = CommandCollection.getDefaultCommandCollection().getCommand(issuedCommand);
        
    	if (command != null) {
    		lastCommandResult = command.execute(issuedCommand);
    	} else {
    		Messenger.printInvalidCommandMessage(issuedCommand.getFirstToken());
    	}
    }
	
	public static void exit() {
		if (gameState != null) {
			if (!gameState.isSaved()) {
				Loader.saveGame(gameState);
			}
			DLogger.info("Exited with no problems.");
		}
		System.exit(0);
	}

}
