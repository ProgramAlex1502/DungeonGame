package org.dungeon.achievements.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.dungeon.achievements.UnlockedAchievement;

class DateUnlockedAchievementComparator implements Comparator<UnlockedAchievement>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(UnlockedAchievement a, UnlockedAchievement b) {
		return a.getDate().compareTo(b.getDate());
	}

}
