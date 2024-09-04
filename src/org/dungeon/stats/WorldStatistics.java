package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.util.CounterMap;

public class WorldStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<String> spawnCounter = new CounterMap<String>();
	private final CounterMap<String> locationCounter = new CounterMap<String>();
	private int spawnCount;
	private int locationCount;
	
	public void addSpawn(String creature) {
		spawnCount++;
		spawnCounter.incrementCounter(creature);
	}
	
	public void addLocation(String location) {
		locationCount++;
		locationCounter.incrementCounter(location);
	}
	
	public int getSpawnCount() {
		return spawnCount;
	}
	
	public int getLocationCount() {
		return locationCount;
	}
	
	public CounterMap<String> getSpawnCounter() {
		return spawnCounter;
	}
	
	public CounterMap<String> getLocationCounter() {
		return locationCounter;
	}

}
