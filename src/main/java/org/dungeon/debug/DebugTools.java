package main.java.org.dungeon.debug;

import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.game.Command;
import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.GameData;
import main.java.org.dungeon.game.GameState;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.game.Location;
import main.java.org.dungeon.game.Point;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.items.ItemBlueprint;
import main.java.org.dungeon.util.Utils;

public class DebugTools {
	
	private static List<Command> commands = new ArrayList<Command>();
	private static boolean uninitialized = true;
	
	public static void parseDebugCommand(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			if (uninitialized) {
				initialize();
			}
			
			for (Command command : commands) {
				if (issuedCommand.firstArgumentEquals(command.name)) {
					command.execute(issuedCommand);
					return;
				}
			}
			IO.writeString("Command not recognized.");
		} else {
			Utils.printMissingArgumentsMessage();
		}
	}
	
	private static void initialize() {
		commands.add(new Command("exploration") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				IO.writeString(Game.getGameState().getStatistics().getExplorationStatistics().toString());
			}
		});
		commands.add(new Command("tomorrow") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				Game.getGameState().getWorld().rollDate(24 * 60 * 60);
				IO.writeString("A day has passed.");
			}
		});
		commands.add(new Command("location") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				printCurrentLocationInformation();
			}
		});
		commands.add(new Command("saved") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				printIsSaved();
			}
		});
		commands.add(new Command("list") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				listAllArguments();
			}
		});
		commands.add(new Command("wait") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				DebugTools.wait(issuedCommand);
			}
		});
		commands.add(new Command("time") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				printTime();
			}
		});
		commands.add(new Command("give") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				if (issuedCommand.getTokenCount() >= 3) {
					give(issuedCommand.getArguments()[1]);
				} else {
					Utils.printMissingArgumentsMessage();
				}
			}
		});
		commands.add(new Command("dummy") {
			@Override
			public void execute(IssuedCommand issuedCommand) {
				spawnDummyInHeroLocation();
			}
		});
		uninitialized = false;
	}
	
	private static void printCurrentLocationInformation() {
		final int WIDTH = 20;
		GameState gameState = Game.getGameState();
		Point heroPosition = gameState.getHeroPosition();
		Location location = gameState.getWorld().getLocation(heroPosition);
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.padString("Point:", WIDTH)).append(heroPosition.toString()).append('\n');
		sb.append(Utils.padString("Creatures (" + location.getCreatureCount() + "):", WIDTH)).append('\n');
		for (Creature creature : location.getCreatures()) {
			sb.append(" ").append(creature.getName()).append('\n');
		}
		if (location.getItemCount() != 0) {
			sb.append(Utils.padString("Items (" + location.getItemCount() + "):", WIDTH)).append('\n');
			for (Item item : location.getItemList()) {
				sb.append(" ").append(item.getQualifiedName()).append('\n');
			}
		} else {
			sb.append("No items.\n");
		}
		sb.append(Utils.padString("Luminosity:", WIDTH)).append(location.getLuminosity()).append('\n');
		sb.append(Utils.padString("Permittivity:", WIDTH)).append(location.getLightPermittivity()).append('\n');
		IO.writeString(sb.toString());
	}
	
	private static void give(String itemID) {
		ItemBlueprint bp = GameData.getItemBlueprints().get(new ID(itemID.toUpperCase()));
		if (bp != null) {
			if (Game.getGameState().getHero().getInventory().addItem(new Item(bp))) {
				return;
			}
		}
		IO.writeString("Item could not be added to your inventory.");
	}
	
	private static void listAllArguments() {
		StringBuilder builder = new StringBuilder();
		builder.append("Valid commands:");
		for (Command command : commands) {
			builder.append("\n ").append(command.name);
		}
		IO.writeString(builder.toString());
	}
	
	private static void spawnDummyInHeroLocation() {
		Creature dummy = new Creature(GameData.getCreatureBlueprints().get(new ID("DUMMY")));
		Game.getGameState().getHeroLocation().addCreature(dummy);
		IO.writeString("Spawned a dummy.");
	}
	
	public static void printIsSaved() {
		if (Game.getGameState().isSaved()) {
			IO.writeString("The game is saved.");
		} else {
			IO.writeString("This game state is not saved.");
		}
	}
	
	public static void wait(IssuedCommand issuedCommand) {
		if (issuedCommand.getTokenCount() > 2) {
			try {
				int seconds = Integer.parseInt(issuedCommand.getArguments()[1]);
				Game.getGameState().getWorld().rollDate(seconds);
			} catch (NumberFormatException warn){
				IO.writeString("Not a valid amount of seconds.");
			}
		} else {
			Utils.printMissingArgumentsMessage();
		}
	}
	
	public static void printTime() {
		IO.writeString(Game.getGameState().getWorld().getWorldDate().toTimeString());
	}

}
