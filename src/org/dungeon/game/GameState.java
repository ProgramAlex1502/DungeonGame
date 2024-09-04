package org.dungeon.game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import org.dungeon.achievements.Achievement;
import org.dungeon.achievements.AchievementTracker;
import org.dungeon.achievements.UnlockedAchievement;
import org.dungeon.creatures.Hero;
import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.stats.Statistics;
import org.dungeon.util.CommandHistory;

public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CommandHistory commandHistory;
	private final World world;
	
	private final Statistics statistics = new Statistics();
	
	private Hero hero;
	private Point heroPosition;
	
	transient private boolean saved;
	
	public GameState() {
		commandHistory = new CommandHistory();
		world = new World(statistics.getWorldStatistics());
		
		createHeroAndStartingLocation();
		
		saved = true;
	}
	
	private void createHeroAndStartingLocation() {
		hero = new Hero();
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
	
	public void printNextHint() {
		if (GameData.getHintLibrary().getHintCount() == 0) {
			IO.writeString("No hints were loaded.");
		} else {
			IO.writeString(GameData.getHintLibrary().getNextHint());
		}
	}
	
	public void printPoem(IssuedCommand command) {
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
	
	public void printUnlockedAchievements() {
		Date now = world.getWorldDate();
		AchievementTracker tracker = hero.getAchievementTracker();
		ArrayList<Achievement> achievements = new ArrayList<Achievement>();
		ArrayList<Period> timeSinceUnlocked = new ArrayList<Period>();
		for (UnlockedAchievement ua : tracker.getUnlockedAchievementArray()) {
			Achievement achievement = GameData.ACHIEVEMENTS.get(ua.id);
			if (achievement != null) {
				achievements.add(achievement);
				timeSinceUnlocked.add(new Period(ua.date, now));
			} else {
				DLogger.warning("Unlocked achievement ID not found in GameData.");
			}
		}
		IO.writeString("Progress: " + tracker.getUnlockedCount() + "/" + GameData.ACHIEVEMENTS.size(), Color.CYAN);
		IO.writeAchievementList(achievements, timeSinceUnlocked);
	}
	
	public Location getHeroLocation() {
		return world.getLocation(heroPosition);
	}

}
