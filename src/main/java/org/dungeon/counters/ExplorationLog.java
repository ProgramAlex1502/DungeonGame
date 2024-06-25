package main.java.org.dungeon.counters;

import java.io.Serializable;
import java.util.HashMap;

import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.game.Point;

public class ExplorationLog implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final HashMap<Point, ExplorationData> entries;
	
	public ExplorationLog() {
		this.entries = new HashMap<Point, ExplorationData>();
	}
	
	public void addVisit(Point point, ID locationID) {
		if (entries.containsKey(point)) {
			entries.get(point).addVisit();
		} else {
			entries.put(point, new ExplorationData(locationID, 1, 0));
		}
	}
	
	public void addKill(Point point) {
		entries.get(point).addKill();
	}
	
	public int getDistinctVisitCount(ID locationID) {
		int count = 0;
		for (ExplorationData entry : entries.values()) {
			if (entry.getLocationID().equals(locationID)) {
				count++;
			}
		}
		return count;
	}
	
	public int getSameLocationVisitCount(ID locationID) {
		int maximumVisitsToLocationWithThisID = 0;
		for (ExplorationData entry : entries.values()) {
			if (entry.getLocationID().equals(locationID)) {
				if (entry.getVisitCount() > maximumVisitsToLocationWithThisID) {
					maximumVisitsToLocationWithThisID = entry.getVisitCount();
				}
			}
		}
		
		return maximumVisitsToLocationWithThisID;
	}
	
	public int getKillCount(ID locationID) {
		int count = 0;
		for (ExplorationData entry : entries.values()) {
			if (entry.getLocationID().equals(locationID)) {
				count += entry.getKillCount();
			}
		}
		return count;
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
