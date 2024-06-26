package main.java.org.dungeon.game;

import java.awt.Color;
import java.io.Serializable;

import main.java.org.dungeon.achievements.Achievement;
import main.java.org.dungeon.achievements.AchievementTracker;
import main.java.org.dungeon.achievements.UnlockedAchievement;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.date.Date;
import main.java.org.dungeon.date.Period;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.stats.Statistics;
import main.java.org.dungeon.util.CommandHistory;

public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CommandHistory commandHistory;
	private final World world;
	
	private final Statistics statistics = new Statistics();
	
	private Hero hero;
	private Point heroPosition;
	
	private boolean bold;
	
	transient private boolean saved;
	private int nextHintIndex;
	private int nextPoemIndex;
	
	public GameState() {
		commandHistory = new CommandHistory();
		
		world = new World(statistics.getWorldStatistics());
		
		createHeroAndStartingLocation();
		
		saved = true;
	}
	
	private void createHeroAndStartingLocation() {
		hero = new Hero("Seth");
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
	
	public boolean isBold() {
		return bold;
	}
	
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
	int getNextHintIndex() {
		return nextHintIndex;
	}
	
	private void setNextHintIndex(int nextHintIndex) {
		this.nextHintIndex = nextHintIndex;
	}
	
	private void incrementNextHintIndex() {
		int newIndex = getNextHintIndex() + 1;
		if (newIndex == GameData.getHintLibrary().getHintCount()) {
			setNextHintIndex(0);
		} else {
			setNextHintIndex(newIndex);
		}
	}
	
	public void printNextHint() {
		if (GameData.getHintLibrary().getHintCount() == 0) {
			IO.writeString("No hints were loaded.");
		} else {
			IO.writeString(GameData.getHintLibrary().getHint(getNextHintIndex()));
			incrementNextHintIndex();
		}
	}
	
	int getNextPoemIndex() {
		return nextPoemIndex;
	}
	
	private void setNextPoemIndex(int nextPoemIndex) {
		this.nextPoemIndex = nextPoemIndex;
	}
	
	private void incrementNextPoemIndex() {
		int newIndex = getNextPoemIndex() + 1;
		if (newIndex == GameData.getPoetryLibrary().getPoemCount()) {
			setNextPoemIndex(0);
		} else {
			setNextPoemIndex(newIndex);
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
				IO.writePoem(GameData.getPoetryLibrary().getPoem(nextPoemIndex));
				incrementNextPoemIndex();
			}
		}
	}
	
	public void printUnlockedAchievements() {
		String dateDifference;
		Achievement achievement;
		Date now = world.getWorldDate();
		AchievementTracker tracker = hero.getAchievementTracker();
		IO.writeString("Progress: " + tracker.getUnlockedCount() + "/" + GameData.ACHIEVEMENTS.size(), Color.CYAN);
		
		for (UnlockedAchievement ua : tracker.getUnlockedAchievementArray()) {
			achievement = GameData.ACHIEVEMENTS.get(ua.id);
			if (achievement != null) {
				dateDifference = new Period(ua.date, now).toString();
				IO.writeString(achievement.getName() + " (" + dateDifference + " ago)", Color.ORANGE);
				IO.writeString(" " + achievement.getInfo(), Color.YELLOW);
			} else {
				DLogger.warning("Unlocked achievement ID not found in GameData");
			}
		}
	}
	
	public void printStatistics(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments() && issuedCommand.firstArgumentEquals("spawns")) {
			statistics.printSpawnStatistics();
		} else {
			statistics.printAllStatistics();
		}
	}
	
	public Location getHeroLocation() {
		return world.getLocation(heroPosition);
	}

}
