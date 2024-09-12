package org.dungeon.achievements;

import java.util.Collection;

import org.dungeon.game.ID;
import org.dungeon.util.CounterMap;

public class Achievement implements Comparable<Achievement> {
	
	private final ID id;
	private final String name;
	private final String info;
	private final String text;
	
	private final BattleComponent battle;
	private final ExplorationComponent exploration;
	
	public Achievement(String id, String name, String info, String text, Collection<BattleStatisticsRequirement> battleRequirements,
			CounterMap<ID> killsByLocationID, CounterMap<ID> visitedLocations, CounterMap<ID> maximumNumberOfVisits) {
		this.id = new ID(id);
		this.name = name;
		this.info = info;
		this.text = text;
		battle = new BattleComponent(battleRequirements);
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
	
	public String getText() {
		return text;
	}
	
	boolean isFulfilled() {
		return battle.isFulfilled() && exploration.isFulfilled();
	}
	
	@Override
	public int compareTo(Achievement achievement) {
		return name.compareTo(achievement.name);
	}
	
	@Override
	public String toString() {
		return String.format("Achievement{id=%s, name='%s', info='%s', text='%s'}", id, name, info, text);
	}

}
