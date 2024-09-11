package org.dungeon.achievements.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.dungeon.achievements.UnlockedAchievement;

class NameUnlockedAchievementComparator implements Comparator<UnlockedAchievement>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(UnlockedAchievement a, UnlockedAchievement b) {
		return a.getName().compareTo(b.getName());
	}

}
