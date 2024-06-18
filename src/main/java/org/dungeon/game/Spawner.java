package main.java.org.dungeon.game;

import main.java.org.dungeon.creatures.Creature;

public class Spawner {
	
	//TODO: finish Spawner class
	
	private final String id;
	private final int populationLimit;
	private final int spawnDelay;
	private final Location location;
	private long lastChange;
	
	public Spawner(SpawnerPreset preset, Location location) {
		id = preset.id;
		populationLimit = preset.population;
		spawnDelay = preset.spawnDelay;
		this.location = location;
		lastChange = getWorldCreationTime();
	}
	
	public void notifyKill(Creature creature) {
		if (id.equals(creature.getId()) && location.getCreatureCount(id) == populationLimit) {
			lastChange = getWorldTime();
		}
	}
	
	private long getWorldTime() {
		return location.getWorld().getWorldDate().getMillis();
	}
	
	private long getWorldCreationTime() {
		return location.getWorld().getWorldCreationDate().getMillis();
	}

}
