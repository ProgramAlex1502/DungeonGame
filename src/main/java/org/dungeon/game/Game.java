package main.java.org.dungeon.game;

import main.java.org.dungeon.gui.GameWindow;
import main.java.org.dungeon.help.Help;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.io.Loader;
import main.java.org.dungeon.util.Math;
import main.java.org.dungeon.util.SystemInfo;
import main.java.org.dungeon.util.Utils;

public class Game {
	
	private static TurnResult result = new TurnResult();	
	private static GameWindow gameWindow;
	private static GameState gameState;
	
	public static void main(String[] args) {
		Help.initialize();
		DLogger.initialize();
		GameData.loadGameData();
		gameWindow = new GameWindow();
		gameState = Loader.loadGame(null);
	}
	
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
			gameState.getWorld().rollDate(result.turnLength);
			Engine.refresh();
			
			if (result.gameStateChanged()) {
				gameState.setSaved(false);
			}
		}
	}
	
	private static void processInput(IssuedCommand issuedCommand) {
		gameState.getCommandHistory().addCommand(issuedCommand);
        gameState.getStatistics().addCommand(issuedCommand);

        if (issuedCommand.firstTokenEquals("rest")) {
            result.turnLength = gameState.getHero().rest();
        } else if (issuedCommand.firstTokenEquals("sleep")) {
            result.turnLength = gameState.getHero().sleep();
        } else if (issuedCommand.firstTokenEquals("look") || issuedCommand.firstTokenEquals("peek")) {
            gameState.getHero().look(null);
        } else if (issuedCommand.firstTokenEquals("inventory") || issuedCommand.firstTokenEquals("items")) {
            gameState.getHero().printInventory();
        } else if (issuedCommand.firstTokenEquals("loot") || issuedCommand.firstTokenEquals("pick")) {
            gameState.getHero().pickItem(issuedCommand);
            result.turnLength = 120;
        } else if (issuedCommand.firstTokenEquals("equip")) {
            gameState.getHero().parseEquip(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("unequip")) {
            gameState.getHero().unequipWeapon();
        } else if (issuedCommand.firstTokenEquals("eat") || issuedCommand.firstTokenEquals("devour")) {
            gameState.getHero().eatItem(issuedCommand);
            result.turnLength = 120;
        } else if (issuedCommand.firstTokenEquals("read")) {
            result.turnLength = gameState.getHero().readItem(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("walk") || issuedCommand.firstTokenEquals("go")) {
            result.turnLength = Engine.parseHeroWalk(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("drop")) {
            gameState.getHero().dropItem(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("destroy") || issuedCommand.firstTokenEquals("crash")) {
            gameState.getHero().destroyItem(issuedCommand);
            result.turnLength = 120;
        } else if (issuedCommand.firstTokenEquals("status")) {
            gameState.getHero().printAllStatus();
        } else if (issuedCommand.firstTokenEquals("skills")) {
            gameState.getHero().printSkills();
        }  else if (issuedCommand.firstTokenEquals("rotation")) {
            gameState.getHero().editRotation(issuedCommand);
        }else if (issuedCommand.firstTokenEquals("hero") || issuedCommand.firstTokenEquals("me")) {
            gameState.getHero().printHeroStatus();
        } else if (issuedCommand.firstTokenEquals("age")) {
            gameState.getHero().printAge();
        } else if (issuedCommand.firstTokenEquals("weapon")) {
            gameState.getHero().printWeaponStatus();
        } else if (issuedCommand.firstTokenEquals("kill") || issuedCommand.firstTokenEquals("attack")) {
            result.turnLength = gameState.getHero().attackTarget(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("statistics")) {
            gameState.printGameStatistics();
        } else if (issuedCommand.firstTokenEquals("achievements")) {
            gameState.printUnlockedAchievements();
        } else if (issuedCommand.firstTokenEquals("time") || issuedCommand.firstTokenEquals("date")) {
            result.turnLength = gameState.getHero().printDateAndTime();
        } else if (issuedCommand.firstTokenEquals("system")) {
            SystemInfo.printSystemInfo();
        } else if (issuedCommand.firstTokenEquals("help") || issuedCommand.firstTokenEquals("?")) {
            Help.printHelp(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("commands")) {
            Help.printCommandList(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("save")) {
            Loader.saveGame(gameState, issuedCommand);
        } else if (issuedCommand.firstTokenEquals("saves")) {
        	Loader.printFilesInSavesFolder();
        } else if (issuedCommand.firstTokenEquals("load")) {
        
            GameState loadedGameState = Loader.loadGame(issuedCommand);
            if (loadedGameState != null) {
                gameState = loadedGameState;
            }
        } else if (issuedCommand.firstTokenEquals("quit") || issuedCommand.firstTokenEquals("exit")) {
            Game.exit();
        } else if (issuedCommand.firstTokenEquals("license") || issuedCommand.firstTokenEquals("copyright")) {
            Utils.printLicense();
        } else if (issuedCommand.firstTokenEquals("fibonacci")) {
            Math.fibonacci(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("hint") || issuedCommand.firstTokenEquals("tip")) {
            gameState.printNextHint();
        } else if (issuedCommand.firstTokenEquals("poem")) {
            gameState.printPoem(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("debug")) {
            DebugTools.parseDebugCommand(issuedCommand);
        } else if (issuedCommand.firstTokenEquals("config")) {
            result.configurationsChanged = ConfigTools.parseConfigCommand(issuedCommand);
        } else {
            Utils.printInvalidCommandMessage(issuedCommand.getFirstToken());
        }
	}
	
	public static void exit() {
		if (!gameState.isSaved()) {
			Loader.saveGame(gameState);
		}
		
		DLogger.info("Exited with no problems.");
		System.exit(0);
	}

}
