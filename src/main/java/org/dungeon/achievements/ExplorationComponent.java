package main.java.org.dungeon.achievements;

import main.java.org.dungeon.counters.ExplorationLog;
import main.java.org.dungeon.creatures.Hero;

public class ExplorationComponent extends AchievementComponent{

	int killCount;
	int visitCount;
	
	public ExplorationComponent() {
		
	}
	
	@Override
	boolean isFulfilled(Hero hero) {
		ExplorationLog explorationLog = hero.getExplorationLog();
		return killCount <= explorationLog.getMaximumKills() && visitCount <= explorationLog.getMaximumVisits();
	}

}
