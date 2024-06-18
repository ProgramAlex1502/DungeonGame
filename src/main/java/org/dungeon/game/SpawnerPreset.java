package main.java.org.dungeon.game;

public class SpawnerPreset {
	
	public final String id;
	public final int population;
	public final int spawnDelay;
	
	public SpawnerPreset(String id, int population, int delayInHours) {
		this.id = id;
		this.population = population;
		this.spawnDelay = delayInHours * 60 * 60 * 1000;
	}

}
