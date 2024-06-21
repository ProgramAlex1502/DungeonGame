package main.java.org.dungeon.game;

import java.awt.Color;
import java.io.Serializable;

import org.joda.time.DateTime;

import main.java.org.dungeon.achievements.Achievement;
import main.java.org.dungeon.achievements.AchievementTracker;
import main.java.org.dungeon.achievements.UnlockedAchievement;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.utils.CommandHistory;
import main.java.org.dungeon.utils.Hints;
import main.java.org.dungeon.utils.Statistics;
import main.java.org.dungeon.utils.Utils;

public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CommandHistory commandHistory;
	private final World world;
	
	private final Statistics statistics;
	
	private final Hero hero;
	private Point heroPosition;
	
	private boolean bold;
	
	private boolean usingBars;
	
	transient private boolean saved;
	private int nextHintIndex;
	private int nextPoemIndex;
	
	public GameState() {
		commandHistory = new CommandHistory();
		world = new World();
		
		statistics = new Statistics();
		
		hero = new Hero("Seth");
		heroPosition = new Point(0, 0);
		
		world.getLocation(heroPosition).addCreature(hero);
		hero.getExplorationLog().addVisit(heroPosition);
		
		saved = true;
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
	
	public boolean isUsingBars() {
		return usingBars;
	}
	
	public void setUsingBars(boolean usingBars) {
		this.usingBars = usingBars;
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
		if (newIndex == Hints.hintsArray.length) {
			setNextHintIndex(0);
		} else {
			setNextHintIndex(newIndex);
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
		if (newIndex == GameData.POEMS.size()) {
			setNextPoemIndex(0);
		} else {
			setNextPoemIndex(newIndex);
		}
	}
	
	public void printUnlockedAchievements() {
		String dateDifference;
		Achievement achievement;
		DateTime now = world.getWorldDate();
		AchievementTracker tracker = hero.getAchievementTracker();
		IO.writeString("Progress: " + tracker.getUnlockedCount() + "/" + GameData.ACHIEVEMENTS.size(), Color.CYAN);
		
		for (UnlockedAchievement ua : tracker.getUnlockedAchievementArray()) {
			achievement = GameData.ACHIEVEMENTS.get(ua.id);
			if (achievement != null) {
				dateDifference = Utils.dateDifferenceToString(ua.date, now);
				IO.writeString(achievement.getName() + " (" + dateDifference + " ago)", Color.ORANGE);
				IO.writeString(" " + achievement.getInfo(), Color.YELLOW);
			} else {
				DLogger.warning("Unlocked achievement ID not found in GameData");
			}
		}
	}
	
	public void printNextHint() {
		IO.writeString(Hints.hintsArray[getNextHintIndex()]);
		incrementNextHintIndex();
	}
	
	public void printNextPoem() {
		if (GameData.POEMS.isEmpty()) {
			IO.writeString("No poems were loaded.", Color.RED);
		} else {
			IO.writePoem(GameData.POEMS.get(nextPoemIndex));
			incrementNextPoemIndex();
		}
	}
	
	public void printGameStatistics() {
		statistics.print();
	}
	
	public Location getHeroLocation() {
		return world.getLocation(heroPosition);
	}

}
