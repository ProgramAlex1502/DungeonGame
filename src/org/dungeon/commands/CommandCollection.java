package org.dungeon.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dungeon.achievements.AchievementTrackerWriter;
import org.dungeon.debug.DebugTools;
import org.dungeon.game.Engine;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.GameState;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.map.WorldMap;
import org.dungeon.map.WorldMapWriter;
import org.dungeon.util.DungeonMath;
import org.dungeon.util.SystemInfo;
import org.dungeon.util.Utils;
import org.dungeon.wiki.Wiki;

public final class CommandCollection {
	
	private static final CommandCollection defaultCommandCollection = new CommandCollection();
	
	static {
		addCommandToDefault(new Command("achievements", "Displays the achievements the character already unlocked.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		AchievementTrackerWriter.parseCommand(issuedCommand);
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("age", "Displays the character's age.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Game.getGameState().getHero().printAge();
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("commands", "Displays a list of valid commands.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		CommandHelp.printCommandList(issuedCommand);
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("debug", "Invokes a debugging command.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		return DebugTools.parseDebugCommand(issuedCommand);
	    	}
	    });
		addCommandToDefault(new Command("destroy", "Destroys an item on the ground.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().destroyItem(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("drop", "Drops the specified item.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().dropItem(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}	
	    });
		addCommandToDefault(new Command("eat", "Eats an item.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().eatItem(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("equip", "Equips the specified item.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().parseEquip(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("exit", "Exits the game.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Game.exit();
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("fibonacci", "Displays the specified term of the Fibonacci's sequence.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		DungeonMath.parseFibonacci(issuedCommand);
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("go", "Makes the character move in the specified direction.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Engine.parseHeroWalk(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("help", "Displays the help text for a specified command.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		CommandHelp.printHelp(issuedCommand);
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("hint", "Displays a random hint of the game.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		GameState.printNextHint();
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("items", "Lists the items in the character's inventory.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Game.getGameState().getHero().printInventory();
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("kill", "Attacks the target chosen by the player.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().attackTarget(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("license", "Displays the game's license.") {
		    @Override
		    public CommandResult execute(IssuedCommand issuedCommand) {
		    	Utils.printLicense();
		    	return null;
		    }
	    });
		addCommandToDefault(new Command("load", "Loads a saved game.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		GameState loadedGameState = Loader.loadGame(issuedCommand);
	    		if (loadedGameState != null) {
	    			Game.setGameState(loadedGameState);
	    		}
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("look", "Describes what the character can see.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Game.getGameState().getHero().look(null);
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("map", "Shows a map of your surroundings.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		WorldMapWriter.writeMap(WorldMap.makeWorldMap());
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("milk", "Attempts to milk a creature.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().parseMilk(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("new", "Starts a new game.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Game.setGameState(null);
	    		Game.setGameState(Loader.newGame());
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("pick", "Attempts to pick up an item from the current location.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().pickItem(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("poem", "Prints a poem from the poem library.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		GameState.printPoem(issuedCommand);
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("read", "Reads the specified item.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().readItem(issuedCommand);
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("repair", "Attempts to cast Repair on the equipped item.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().castRepairOnEquippedItem();
	    		return new SimpleCommandResult(duration);
	    	}
	    });
		addCommandToDefault(new Command("rest", "Rests until healing about three fifths of the character's health.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().rest();
	    		return new SimpleCommandResult(duration);
	    }
	    });
		addCommandToDefault(new Command("rotation", "Either displays the current skill rotation or sets up a new one.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Game.getGameState().getHero().editRotation(issuedCommand);
	    		return null;
	    	}
	    });
		addCommandToDefault(new Command("save", "Saves the game.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Loader.saveGame(Game.getGameState(), issuedCommand);
	    		return null;
	    	}
	    });
	    addCommandToDefault(new Command("saves", "Displays a table with all the save files.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Loader.printFilesInSavesFolder();
	    		return null;
	    	}
	    });
	    addCommandToDefault(new Command("skills", "Displays a list with all skills known by the character.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Game.getGameState().getHero().printSkills();
	    		return null;
	    	}
	    });
	    addCommandToDefault(new Command("sleep", "Sleeps until the sun rises. The character may dream during it.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().sleep();
	    		return new SimpleCommandResult(duration);
	    	}
	    });
	    addCommandToDefault(new Command("statistics", "Displays all available game statistics.") {
			@Override
			public CommandResult execute(IssuedCommand issuedCommand) {
				Game.getGameState().getStatistics().printStatistics();
				return null;
			}
	    });
	    addCommandToDefault(new Command("status", "Displays the character's status.") {
		    @Override
		    public CommandResult execute(IssuedCommand issuedCommand) {
		    	Game.getGameState().getHero().printAllStatus();
		    	return null;
		    }
	    });
	    addCommandToDefault(new Command("system", "Displays information about the underlying system.") {
	    	@Override
	      	public CommandResult execute(IssuedCommand issuedCommand) {
	    		SystemInfo.printSystemInfo();
	    		return null;
	      	}
	    });
	    addCommandToDefault(new Command("time", "Displays what the character knows about the current time and date.") {
	    	@Override
	      	public CommandResult execute(IssuedCommand issuedCommand) {
	    	  	int duration = Game.getGameState().getHero().printDateAndTime();
	    	  	return new SimpleCommandResult(duration);
	      	}
	    });
	    addCommandToDefault(new Command("tutorial", "Displays the tutorial.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		IO.writeString(GameData.getTutorial());
	    		return null;
	    	}
	    });
	    addCommandToDefault(new Command("unequip", "Unequips the currently equipped item.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		int duration = Game.getGameState().getHero().unequipWeapon();
	    		return new SimpleCommandResult(duration);
	    	}
	    });
	    addCommandToDefault(new Command("wiki", "Searches the wiki for an article.") {
	    	@Override
	    	public CommandResult execute(IssuedCommand issuedCommand) {
	    		Wiki.search(issuedCommand);
	    		return null;
	    	}
	    });
	}
	
	private final List<Command> commands = new ArrayList<Command>();
	private final List<CommandDescription> commandDescriptions = new ArrayList<CommandDescription>();
	
	private CommandCollection() {
		
	}
	
	public static CommandCollection getDefaultCommandCollection() {
		return defaultCommandCollection;
	}
	
	private static void addCommandToDefault(Command command) {
		defaultCommandCollection.addCommand(command);
	}
	
	public Command getCommand(IssuedCommand issuedCommand) {
		if (issuedCommand == null) {
			DLogger.warning("Passed null IssuedCommand to CommandCollection.getCommand()!");
		} else {
			for (Command command : commands) {
				if (command.getDescription().getName().equalsIgnoreCase(issuedCommand.getFirstToken())) {
					return command;
				}
			}
		}
		return null;
	}
	
	private void addCommand(Command command) {
		if (command == null) {
			DLogger.warning("Passed null to CommandCollection.addCommand()!");
		} else if (commands.contains(command)) {
			DLogger.warning("Attempted to add the same Command to a CommandCollection twice!");
		} else {
			commands.add(command);
			commandDescriptions.add(command.getDescription());
		}
	}
	
	List<CommandDescription> getCommandDescriptions() {
		return Collections.unmodifiableList(commandDescriptions);
	}
	
	@Override
	public String toString() {
		return String.format("CommandCollection of size %d.", commands.size());
	}

}
