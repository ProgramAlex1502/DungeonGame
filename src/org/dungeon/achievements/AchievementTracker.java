package org.dungeon.achievements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dungeon.date.Date;
import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;

public class AchievementTracker implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Set<UnlockedAchievement> unlockedAchievements = new HashSet<UnlockedAchievement>();
	
	public int getUnlockedCount() {
		return unlockedAchievements.size();
	}
	
	public void unlock(Achievement achievement) {
		Date now = Game.getGameState().getWorld().getWorldDate();
		if (!isUnlocked(achievement)) {
			unlockedAchievements.add(new UnlockedAchievement(achievement, now));
		} else {
			DLogger.warning("Tried to unlock an already unlocked achievement!");
		}
	}
	
	private UnlockedAchievement getUnlockedAchievement(Achievement achievement) {
		ID id = achievement.getID();
		for (UnlockedAchievement ua : unlockedAchievements) {
			if (ua.id.equals(id)) {
				return ua;
			}
		}
		
		return null;
	}
	
	public List<UnlockedAchievement> getUnlockedAchievements(Comparator<UnlockedAchievement> comparator) {
		if (comparator == null) {
			throw new IllegalArgumentException("comparator is null");
		}
		List<UnlockedAchievement> list = new ArrayList<UnlockedAchievement>(unlockedAchievements);
		Collections.sort(list, comparator);
		return list;
	}
	
	public boolean isUnlocked(Achievement achievement) {
		return getUnlockedAchievement(achievement) != null;
	}

}
