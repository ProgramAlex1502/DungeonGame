package main.java.org.dungeon.counters;

import java.io.Serializable;

import main.java.org.dungeon.game.ID;

public class ExplorationData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final ID locationID;
	private int visitCount;
	private int killCount;
	
	public ExplorationData(ID locationID, int visitCount, int killCount) {
		this.locationID = locationID;
		this.visitCount = visitCount;
		this.killCount = killCount;
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

}
