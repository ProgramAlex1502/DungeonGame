package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.game.ID;

class ExplorationStatisticsEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ID locationID;
	private int visitCount;
	private int killCount;
	
	public ExplorationStatisticsEntry(ID locationID) {
		this.locationID = locationID;
	}
	
	public ID getLocationID() {
		return locationID;
	}
	
	public int getVisitCount() {
		return visitCount;
	}
	
	public void addVisit() {
		this.visitCount++;
	}
	
	public int getKillCount() {
		return killCount;
	}
	
	public void addKill() {
		this.killCount++;
	}
	
	@Override
	public String toString() {
		String format = "ExplorationStatisticsEntry{locationID=%s, visitCount=%d, killCount=%d}";
		return String.format(format, locationID, visitCount, killCount);
	}

}
