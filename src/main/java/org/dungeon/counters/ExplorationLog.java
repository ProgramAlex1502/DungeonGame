package main.java.org.dungeon.counters;

import java.io.Serializable;
import java.util.HashMap;

import main.java.org.dungeon.game.Point;

public class ExplorationLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final HashMap<Point, ExplorationData> entries;
	
	private int maximumVisits;
	private int maximumKills;
	
	public ExplorationLog() {
		this.entries = new HashMap<Point, ExplorationData>();
	}
	
	public int getMaximumVisits() {
		return maximumVisits;
	}
	
	public int getMaximumKills() {
		return maximumKills;
	}
	
	private int getVisitCount(Point point) {
		return entries.containsKey(point) ? entries.get(point).getVisitCount() : 0;
	}
	
	public void addVisit(Point point) {
		int visitsToPoint;
		if (entries.containsKey(point)) {
			visitsToPoint = entries.get(point).addVisit();
		} else {
			entries.put(point, new ExplorationData(1, 0));
			visitsToPoint = 1;
		}
		
		if (visitsToPoint > maximumVisits) {
			maximumVisits = visitsToPoint;
		}
	}
	
	private int getKillCount(Point point) {
		return entries.containsKey(point) ? entries.get(point).getKillCount() : 0;
	}
	
	public void addKill(Point point) {
		int killsInPoint = entries.get(point).addKill();
		if (killsInPoint > maximumKills) {
			maximumKills = killsInPoint;
		}
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		ExplorationData explorationData;
		
		for (Point point : entries.keySet()) {
			explorationData = entries.get(point);
			builder.append(point.toString());
			builder.append(" Visits: ").append(explorationData.getVisitCount());
			builder.append(" Kills: ").append(explorationData.getKillCount());
			builder.append("\n");
		}
		
		return builder.toString();
	}

}
