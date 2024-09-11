package org.dungeon.game;

import java.io.Serializable;

import org.dungeon.commands.CommandHistory;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.entity.creatures.CreatureFactory;
import org.dungeon.entity.creatures.Hero;
import org.dungeon.io.IO;
import org.dungeon.io.JsonObjectFactory;
import org.dungeon.stats.Statistics;

public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CommandHistory commandHistory;
	private final World world;
	
	private final Statistics statistics = new Statistics();
	
	private Hero hero;
	private Point heroPosition;
	
	transient private boolean saved = false;
	
	public GameState() {
		commandHistory = new CommandHistory();
		world = new World(statistics.getWorldStatistics());
		
		createHeroAndStartingLocation();
	}
	
	public static void printNextHint() {
		if (GameData.getHintLibrary().getHintCount() == 0) {
			IO.writeString("No hints were loaded.");
		} else {
			IO.writeString(GameData.getHintLibrary().getNextHint());
		}
	}
	
	public static void printPoem(IssuedCommand command) {
		if (GameData.getPoetryLibrary().getPoemCount() == 0) {
			IO.writeString("No poems were loaded.");
		} else {
			if (command.hasArguments()) {
				try {
					int index = Integer.parseInt(command.getFirstArgument()) - 1;
					if (index >= 0 && index < GameData.getPoetryLibrary().getPoemCount()) {
						IO.writePoem(GameData.getPoetryLibrary().getPoem(index));
						return;
					}
				} catch (NumberFormatException ignore) {
				}
				IO.writeString("Invalid poem index.");
			} else {
				IO.writePoem(GameData.getPoetryLibrary().getNextPoem());
			}
		}
	}
	
	public String getPreface() {
		return String.format(JsonObjectFactory.makeJsonObject("preface.json").get("format").asString(), hero.getLocation());
	}
	
	private void createHeroAndStartingLocation() {
		hero = CreatureFactory.makeHero(world.getWorldDate());
		heroPosition = new Point(0, 0);
		
		world.getLocation(heroPosition).addCreature(hero);
		getStatistics().getExplorationStatistics().addVisit(heroPosition, world.getLocation(heroPosition).getID());		
	}
	
	public CommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Statistics getStatistics() {
		return statistics;
	}
	
	public Hero getHero() {
		return hero;
	}
	
	public Point getHeroPosition() {
		return heroPosition;
	}
	
	public void setHeroPosition(Point heroPosition) {
		this.heroPosition = heroPosition;
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
	
	
	public Location getHeroLocation() {
		return world.getLocation(heroPosition);
	}

}
