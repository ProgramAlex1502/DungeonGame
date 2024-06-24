package main.java.org.dungeon.counters;

import java.io.Serializable;

public class ExplorationData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private String locationID;
	private int visitCount;
	private int killCount;
	
	public ExplorationData(int visitCount, int killCount) {
		this.visitCount = visitCount;
		this.killCount = killCount;
	}
	
	public int getVisitCount() {
		return visitCount;
	}
	
	public int addVisit() {
		return ++this.visitCount;
	}
	
	public int getKillCount() {
		return killCount;
	}
	
	public int addKill() {
		return ++this.killCount;
	}

}
