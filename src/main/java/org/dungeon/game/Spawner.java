package main.java.org.dungeon.game;

import java.io.Serializable;

import main.java.org.dungeon.creatures.Creature;

public class Spawner implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
	
	public void refresh() {
		long worldTime = getWorldTime();
		
		while (worldTime - lastChange >= spawnDelay && location.getCreatureCount(id) < populationLimit) {
			location.getWorld().getSpawnCounter().incrementCounter(id);
			location.addCreature(new Creature(GameData.CREATURE_BLUEPRINTS.get(id)));
			
			lastChange += spawnDelay;
		}
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
