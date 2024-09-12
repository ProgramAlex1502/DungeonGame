package org.dungeon.achievements;

import java.util.ArrayList;
import java.util.Collection;

import org.dungeon.game.ID;
import org.dungeon.util.CounterMap;

public class AchievementBuilder {
	
	private final Collection<BattleStatisticsRequirement> requirements = new ArrayList<BattleStatisticsRequirement>();
	private String id;
	private String name;
	private String info;
	private String text;
	private CounterMap<ID> killsByLocationID;
	private CounterMap<ID> visitedLocations;
	private CounterMap<ID> maximumNumberOfVisits;
	
	public void setID(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}

	public void setText(String text) {
	    this.text = text;
	}

	public void addBattleStatisticsRequirement(BattleStatisticsRequirement requirement) {
		requirements.add(requirement);
	}

	public void setKillsByLocationID(CounterMap<ID> killsByLocationID) {
	    if (killsByLocationID.isNotEmpty()) {
	    	this.killsByLocationID = killsByLocationID;
	    }
	}

	public void setVisitedLocations(CounterMap<ID> visitedLocations) {
	    if (visitedLocations.isNotEmpty()) {
	    	this.visitedLocations = visitedLocations;
	    }
	}

	public void setMaximumNumberOfVisits(CounterMap<ID> maximumNumberOfVisits) {
	    if (maximumNumberOfVisits.isNotEmpty()) {
	    	this.maximumNumberOfVisits = maximumNumberOfVisits;
	    }
	}
	
	public Achievement createAchievement() {
		return new Achievement(id, name, info, text, requirements, killsByLocationID, visitedLocations, 
				maximumNumberOfVisits);
	}

}
