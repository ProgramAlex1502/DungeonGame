package org.dungeon.achievements;

import java.io.Serializable;
import java.util.TreeSet;

import org.dungeon.date.Date;
import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;

public class AchievementTracker implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final TreeSet<UnlockedAchievement> unlockedAchievements;
	
	public AchievementTracker() {
		this.unlockedAchievements = new TreeSet<UnlockedAchievement>(new UnlockedAchievementNameComparator());
	}
	
	public int getUnlockedCount() {
		return unlockedAchievements.size();
	}
	
	public void unlock(Achievement achievement) {
		Date now = Game.getGameState().getWorld().getWorldDate();
		if (!isUnlocked(achievement)) {
			unlockedAchievements.add(new UnlockedAchievement(achievement.getID(), achievement.getName(), now));
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
	
	public UnlockedAchievement[] getUnlockedAchievementArray() {
		return unlockedAchievements.toArray(new UnlockedAchievement[unlockedAchievements.size()]);
	}
	
	public boolean isUnlocked(Achievement achievement) {
		return getUnlockedAchievement(achievement) != null;
	}

}
