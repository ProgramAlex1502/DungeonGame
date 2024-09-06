package org.dungeon.achievements;

import org.dungeon.entity.creatures.Hero;
import org.dungeon.game.ID;
import org.dungeon.io.IO;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.util.CounterMap;

public class Achievement {
	
	private final ID id;
	private final String name;
	private final String info;
	private final String text;
	
	private final BattleComponent battle;
	private final ExplorationComponent exploration;
	
	public Achievement(String id, String name, String info, String text, int minimumBattleCount, int longestBattleLength,
			CounterMap<ID> killsByCreatureID, CounterMap<String> killsByCreatureType, CounterMap<CauseOfDeath> killsByCauseOfDeath,
			CounterMap<ID> killsByLocationID, CounterMap<ID> visitedLocations, CounterMap<ID> maximumNumberOfVisits) {
		this.id = new ID(id);
		this.name = name;
		this.info = info;
		this.text = text;
		battle = new BattleComponent(minimumBattleCount, longestBattleLength, killsByCreatureID, killsByCreatureType, killsByCauseOfDeath);
		exploration = new ExplorationComponent(killsByLocationID, visitedLocations, maximumNumberOfVisits);
	}
	
	public ID getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return info;
	}
	
	private boolean isFulfilled() {
		return battle.isFulfilled() && exploration.isFulfilled();
	}
	
	public final void update(Hero hero) {
		if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled()) {
			printAchievementUnlocked();
			hero.getAchievementTracker().unlock(this);
		}
	}
	
	private void printAchievementUnlocked() {
		IO.writeString("You unlocked the achievement " + getName() + " because you " + text + ".");
	}

}
