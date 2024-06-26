package main.java.org.dungeon.game;

import java.io.Serializable;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.creatures.CreatureBlueprint;

class Spawner implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ID id;
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
		CreatureBlueprint blueprint = GameData.getCreatureBlueprints().get(id);
		
		while (worldTime - lastChange >= spawnDelay && location.getCreatureCount(id) < populationLimit) {
			location.addCreature(new Creature(blueprint));
			Game.getGameState().getStatistics().getWorldStatistics().addSpawn(blueprint.getName());
			
			lastChange += spawnDelay;
		}
	}
	
	public void notifyKill(Creature creature) {
		if (id.equals(creature.getID()) && location.getCreatureCount(id) == populationLimit) {
			lastChange = getWorldTime();
		}
	}
	
	private long getWorldTime() {
		return location.getWorld().getWorldDate().getTime();
	}
	
	private long getWorldCreationTime() {
		return location.getWorld().getWorldCreationDate().getTime();
	}

}
