package org.dungeon.stats;

import java.io.Serializable;
import java.util.HashMap;

import org.dungeon.game.ID;
import org.dungeon.game.Point;

public class ExplorationStatistics implements Serializable {
	private static final long serialVersionUID = 1L;

	private final HashMap<Point, ExplorationStatisticsEntry> entries;
	
	public ExplorationStatistics() {
		this.entries = new HashMap<Point, ExplorationStatisticsEntry>();
	}
	
	public void createEntryIfNotExists(Point point, ID locationID) {
		if (!hasBeenSeen(point)) {
			entries.put(point, new ExplorationStatisticsEntry(locationID));
		}
	}
	
	public void addVisit(Point point, ID locationID) {
		createEntryIfNotExists(point, locationID);
		entries.get(point).addVisit();
	}
	
	public void addKill(Point point) {
		entries.get(point).addKill();
	}
	
	public boolean hasBeenSeen(Point point) {
		return entries.containsKey(point);
	}
	
	public int getVisitedLocations(ID locationID) {
		int count = 0;
		for (ExplorationStatisticsEntry entry : entries.values()) {
			if (entry.getLocationID().equals(locationID) && entry.getVisitCount() > 0) {
				count++;
			}
		}
		return count;
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
