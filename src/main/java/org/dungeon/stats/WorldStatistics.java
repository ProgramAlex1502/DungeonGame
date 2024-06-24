package main.java.org.dungeon.stats;

import java.io.Serializable;

public class WorldStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int locations;
	private int creatures;
	
	public void addLocation(String location) {
		locations++;
	}
	
	public void addSpawn(String creature) {
		creatures++;
	}
	
	public int getLocationCount() {
		return locations;
	}
	
	public int getCreatureCount() {
		return creatures;
	}

}
