package main.java.org.dungeon.stats;

import java.io.Serializable;

import main.java.org.dungeon.counters.CounterMap;

public class WorldStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<String> spawnCounter = new CounterMap<String>();
	private int locations;
	private int creatures;
	
	public void addLocation(String location) {
		locations++;
	}
	
	public void addSpawn(String creature) {
		creatures++;
		spawnCounter.incrementCounter(creature);
	}
	
	public int getLocationCount() {
		return locations;
	}
	
	public int getCreatureCount() {
		return creatures;
	}
	
	public CounterMap<String> getSpawnCounters() {
		return spawnCounter;
	}

}
