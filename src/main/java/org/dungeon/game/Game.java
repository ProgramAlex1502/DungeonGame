package main.java.org.dungeon.game;

import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.debug.DebugTools;
import main.java.org.dungeon.gui.GameWindow;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.io.Loader;
import main.java.org.dungeon.util.CommandHelp;
import main.java.org.dungeon.util.Math;
import main.java.org.dungeon.util.SystemInfo;
import main.java.org.dungeon.util.Utils;
import main.java.org.dungeon.wiki.Wiki;

public class Game {
	
	private static final TurnResult turnResult = new TurnResult();	
	private static GameWindow gameWindow;
	private static GameState gameState;
	private static List<Command> commandList;
	
	public static void main(String[] args) {
		DLogger.initialize();
		GameData.loadGameData();
		initializeCommands();
		gameWindow = new GameWindow();
		gameState = Loader.loadGame(null);
		Engine.refresh();
	}
	
	private static void initializeCommands() {
		commandList = new ArrayList<Command>();
		
		commandList.add(new Command("achievements", "Displays the achievements the character already unlocked.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.printUnlockedAchievements();
			}
		});
		commandList.add(new Command("age", "Displays the character's age.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().printAge();
			}
		});
		commandList.add(new Command("commands", "Displays a list of valid commands.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				CommandHelp.printCommandList(issuedCommand);
			}
		});
		commandList.add(new Command("config", "Configures the specified option.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.configurationsChanged = ConfigTools.parseConfigCommand(issuedCommand);
			}
		});
		commandList.add(new Command("debug", "Invokes a debugging command.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				DebugTools.parseDebugCommand(issuedCommand);
			}
		});
		commandList.add(new Command("destroy", "Destroys an item on the ground.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().destroyItem(issuedCommand);
			}
		});
		commandList.add(new Command("drop", "Drops the specified item.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().dropItem(issuedCommand);
			}
		});
		commandList.add(new Command("eat", "Eats an item.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().eatItem(issuedCommand);
			}
		});
		commandList.add(new Command("equip", "Equips the specified item.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().parseEquip(issuedCommand);
			}
		});
		commandList.add(new Command("exit", "Exits the game.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				Game.exit();
			}
		});
		commandList.add(new Command("fibonacci", "Displayers the specified term of the Fibonacci's sequence.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				Math.fibonacci(issuedCommand);
			}
		});
		commandList.add(new Command("go", "Makes the character move in the specified direction.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = Engine.parseHeroWalk(issuedCommand);
			}
		});
		commandList.add(new Command("help", "Displays the help text for a specified command.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				CommandHelp.printHelp(issuedCommand);
			}
		});
		commandList.add(new Command("hint", "Displays a random hint of the game.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.printNextHint();
			}
		});
		commandList.add(new Command("items", "Lists the items in the character's inventory.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().printInventory();
			}
		});
		commandList.add(new Command("kill", "Attacks the target chosen by the player.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().attackTarget(issuedCommand);
			}
		});
		commandList.add(new Command("license", "Displays the game's license.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				Utils.printLicense();
			}
		});
		commandList.add(new Command("load", "Loads a saved game.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				GameState loadedGameState = Loader.loadGame(issuedCommand);
				if (loadedGameState != null) {
					gameState = loadedGameState;
				}
			}
		});
		commandList.add(new Command("look", "Describes what the character can see.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().look(null);
			}
		});
		commandList.add(new Command("pick", "Attempts to pick up an item from the current location.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().pickItem(issuedCommand);
			}
		});
		commandList.add(new Command("poem", "Prints a poem from the poem library") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.printPoem(issuedCommand);
			}
		});
		commandList.add(new Command("read", "Reads the specified item.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().readItem(issuedCommand);
			}
		});
		commandList.add(new Command("rest", "Rests until healing about three fifths of the character's health.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().rest();
			}
		});
		commandList.add(new Command("rotation", "Either displays the current skill rotation or sets up  a new one.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().editRotation(issuedCommand);
			}
		});
		commandList.add(new Command("save", "Saves the game.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				Loader.saveGame(gameState, issuedCommand);
			}
		});
		commandList.add(new Command("saves", "Displays a table with all the save files.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				Loader.printFilesInSavesFolder();
			}
		});
		commandList.add(new Command("skills", "Displays a list with all skills known by the character.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().printSkills();
			}
		});
		commandList.add(new Command("sleep", "Sleeps until the sun rises. The character may dream during it.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().sleep();
			}
		});
		commandList.add(new Command("statistics", "Displays all available game statistics.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.printStatistics(issuedCommand);
			}
		});
		commandList.add(new Command("status", "Displays the character's status") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().printAllStatus();
			}
		});
		commandList.add(new Command("system", "Displays information about the underlying system.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				SystemInfo.printSystemInfo();
			}
		});
		commandList.add(new Command("time", "Displays what the character knows about the current time and date.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				turnResult.turnLength = gameState.getHero().printDateAndTime();
			}
		});
		commandList.add(new Command("unequip", "Unequips the currently equipped item.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				gameState.getHero().unequipWeapon();
			}
		});
		commandList.add(new Command("wiki", "Searches the wiki for an article.") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				Wiki.search(issuedCommand);
			}
		});
	}
	
	public static List<Command> getCommandList() {
		return commandList;
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
			gameState.getWorld().rollDate(turnResult.turnLength);
			Engine.refresh();
			
			if (turnResult.gameStateChanged()) {
				gameState.setSaved(false);
			}
		}
		
		turnResult.clear();
	}
	
	private static void processInput(IssuedCommand issuedCommand) {
		gameState.getCommandHistory().addCommand(issuedCommand);
        gameState.getStatistics().addCommand(issuedCommand);

        for (Command command : commandList) {
        	if (command.name.equalsIgnoreCase(issuedCommand.getFirstToken())) {
        		command.execute(issuedCommand);
        		return;
        	}
        }
        
        Utils.printInvalidCommandMessage(issuedCommand.getFirstToken());

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
