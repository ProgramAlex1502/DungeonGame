package org.dungeon.achievements;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;

import org.dungeon.achievements.comparators.UnlockedAchievementComparators;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.io.IO;

public class AchievementTrackerWriter {
	
	public static void parseCommand(IssuedCommand issuedCommand) {
		Comparator<UnlockedAchievement> comparator = getComparator(issuedCommand);
		if (comparator != null) {
			AchievementTracker achievementTracker = Game.getGameState().getHero().getAchievementTracker();
			writeAchievementTracker(achievementTracker, comparator);
		} else {
			writeValidOrderings();
		}
	}
	
	private static Comparator<UnlockedAchievement> getComparator(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments() && issuedCommand.getFirstArgument().equalsIgnoreCase("by")) {
			if (issuedCommand.getArguments().length > 1) {
				String secondArgument = issuedCommand.getArguments()[1];
				for (String comparatorName : UnlockedAchievementComparators.getComparatorMap().keySet()) {
					if (secondArgument.equalsIgnoreCase(comparatorName)) {
						return UnlockedAchievementComparators.getComparatorMap().get(comparatorName);
					}
				}
			}
			return null;
		} else {
			return UnlockedAchievementComparators.getDefaultComparator();
		}
	}
	
	private static void writeAchievementTracker(AchievementTracker tracker, Comparator<UnlockedAchievement> comparator) {
		List<UnlockedAchievement> unlockedAchievements = tracker.getUnlockedAchievements(comparator);
		Date now = Game.getGameState().getWorld().getWorldDate();
		for (UnlockedAchievement unlockedAchievement : unlockedAchievements) {
			Period sinceUnlock = new Period(unlockedAchievement.getDate(), now);
			IO.writeString(String.format("%s (%s ago)", unlockedAchievement.getName(), sinceUnlock), Color.ORANGE);
			IO.writeString(" " + unlockedAchievement.getInfo(), Color.YELLOW);
		}
		IO.writeString("Progress: " + tracker.getUnlockedCount() + "/" + GameData.ACHIEVEMENTS.size(), Color.CYAN);
	}
	
	private static void writeValidOrderings() {
		IO.writeString("Valid orderings:");
		for (String comparatorName : UnlockedAchievementComparators.getComparatorMap().keySet()) {
			IO.writeString(" " + comparatorName);
		}
	}

}
