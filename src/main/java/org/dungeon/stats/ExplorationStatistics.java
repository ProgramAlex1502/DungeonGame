package main.java.org.dungeon.stats;

import java.io.Serializable;
import java.util.HashMap;

import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.game.Point;

public class ExplorationStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final HashMap<Point, ExplorationStatisticsEntry> entries;
	
	public ExplorationStatistics() {
		this.entries = new HashMap<Point, ExplorationStatisticsEntry>();
	}
	
	public void addVisit(Point point, ID locationID) {
		if (entries.containsKey(point)) {
			entries.get(point).addVisit();
		} else {
			entries.put(point, new ExplorationStatisticsEntry(locationID, 1, 0));
		}
	}
	
	public void addKill(Point point) {
		entries.get(point).addKill();
	}
	
	public int getKillCount(ID locationID) {
		int count = 0;
		for (ExplorationStatisticsEntry entry : entries.values()) {
			if (entry.getLocationID().equals(locationID)) {
				count += entry.getKillCount();
			}
		}
		return count;
	}
	
	public int getDiscoveredLocations(ID locationID) {
		int count = 0;
		for (ExplorationStatisticsEntry entry : entries.values()) {
			if (entry.getLocationID().equals(locationID)) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getMaximumNumberOfVisits(ID locationID) {
		int maximumVisitsToLocationWithThisID = 0;
		for (ExplorationStatisticsEntry entry : entries.values()) {
			if (entry.getLocationID().equals(locationID)) {
				if (entry.getVisitCount() > maximumVisitsToLocationWithThisID) {
					maximumVisitsToLocationWithThisID = entry.getVisitCount();
				}
			}
		}
		return maximumVisitsToLocationWithThisID;
	}

}
