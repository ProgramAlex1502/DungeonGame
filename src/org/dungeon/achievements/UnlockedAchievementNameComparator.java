package org.dungeon.achievements;

import java.io.Serializable;
import java.util.Comparator;

public class UnlockedAchievementNameComparator implements Comparator<UnlockedAchievement>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(UnlockedAchievement a, UnlockedAchievement b) {
		return a.name.compareTo(b.name);
	}

}
