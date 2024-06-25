package main.java.org.dungeon.game;

import java.awt.Color;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.io.Loader;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.items.ItemBlueprint;
import main.java.org.dungeon.util.Utils;

public class DebugTools {
	
	private static final String[] args = {"exploration", "tomorrow", "holidays", "saves", "location", "generator",
			"saved", "list", "time", "give", "dummy"
	};
	
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
		IO.writeString("Arguments are: ");
		
		for(String arg : args) {
			IO.writeString("\n  " + arg);
		}
	}
	
	private static void spawnDummyInHeroLocation() {
		Game.getGameState().getHeroLocation().addCreature(new Creature(GameData.getCreatureBlueprints().get(new ID("DUMMY"))));
		IO.writeString("Spawned a dummy.");
	}
	
	private static void printIsSaved() {
		if (Game.getGameState().isSaved()) {
			IO.writeString("The game is saved.");
		} else {
			IO.writeString("This game state is not saved.");
		}
	}
	
	private static void printTime() {
		IO.writeString(Game.getGameState().getWorld().getWorldDate().toTimeString());
	}
	
	static void parseDebugCommand(IssuedCommand issuedCommand) {
        if (issuedCommand.hasArguments()) {
            if (issuedCommand.firstArgumentEquals(args[0])) {
                IO.writeString(Game.getGameState().getHero().getExplorationLog().toString());
            } else if (issuedCommand.firstArgumentEquals(args[1])) {
                Game.getGameState().getWorld().rollDate(24 * 60 * 60);
                if (Engine.RANDOM.nextBoolean()) {
                    IO.writeString("A day has passed.", Color.ORANGE);
                } else {
                    IO.writeString("You are one day closer to your ending.", Color.ORANGE);
                }
            } else if (issuedCommand.firstArgumentEquals(args[3])) {
                Loader.printFilesInSavesFolder();
            } else if (issuedCommand.firstArgumentEquals(args[4])) {
                final int WIDTH = 20;
                GameState gameState = Game.getGameState();
                Point heroPosition = gameState.getHeroPosition();
                Location location = gameState.getWorld().getLocation(heroPosition);
                StringBuilder sb = new StringBuilder();
                sb.append(Utils.padString("Point:", WIDTH)).append(heroPosition.toString()).append('\n');
                sb.append(Utils.padString("Creatures (" + location.getCreatureCount() + "):", WIDTH)).append('\n');
                for (Creature creature : location.getCreatures()) {
                    sb.append("  ").append(creature.getName()).append('\n');
                }
                if (location.getItemCount() != 0) {
                    sb.append(Utils.padString("Items (" + location.getItemCount() + "):", WIDTH)).append('\n');
                    for (Item item : location.getItemList()) {
                        sb.append("  ").append(item.getQualifiedName()).append('\n');
                    }
                } else {
                    sb.append("No items.\n");
                }
                sb.append(Utils.padString("Luminosity:", WIDTH)).append(location.getLuminosity()).append('\n');
                sb.append(Utils.padString("Permittivity:", WIDTH)).append(location.getLightPermittivity()).append('\n');
                IO.writeString(sb.toString());
            } else if (issuedCommand.firstArgumentEquals(args[5])) {
                Game.getGameState().getWorld().getGenerator().printStatistics();
            } else if (issuedCommand.firstArgumentEquals(args[6])) {
                printIsSaved();
            } else if (issuedCommand.firstArgumentEquals(args[7])) {
                listAllArguments();
            } else if (issuedCommand.firstArgumentEquals(args[8])) {
                printTime();
            } else if (issuedCommand.firstArgumentEquals(args[9])) {
                if (issuedCommand.getTokenCount() >= 3) {
                    give(issuedCommand.getArguments()[1]);
                } else {
                    Utils.printMissingArgumentsMessage();
                }
            } else if (issuedCommand.firstArgumentEquals(args[10])) {
            	spawnDummyInHeroLocation();
            } else {
                switch (Engine.RANDOM.nextInt(4)) {
                    case 0:
                        IO.writeString("FOOL! You cannot understand the power of 'debug'!", Color.RED);
                        break;
                    case 1:
                        IO.writeString("If you do not know how to use the debug command, do not use it.", Color.RED);
                        break;
                    case 2:
                        IO.writeString("Wrong are you.", Color.RED);
                        break;
                    case 3:
                        IO.writeString("Stop trying to cheat, will you?", Color.RED);
                        break;
                }
            }
        } else {
            Utils.printMissingArgumentsMessage();
        }
    }

}
