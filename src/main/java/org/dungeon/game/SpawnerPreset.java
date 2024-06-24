package main.java.org.dungeon.game;

public class SpawnerPreset {
	
	public final ID id;
	public final int population;
	public final int spawnDelay;
	
	public SpawnerPreset(String id, int population, int delayInHours) {
		this.id = new ID(id);
		this.population = population;
		this.spawnDelay = delayInHours * 60 * 60 * 1000;
	}

}
