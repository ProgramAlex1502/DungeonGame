package org.dungeon.game;

import org.dungeon.date.Date;

class SpawnerPreset {
	
	public final ID id;
	public final int population;
	public final int spawnDelay;
	
	public SpawnerPreset(String id, int population, int delayInHours) {
		this.id = new ID(id);
		this.population = population;
		this.spawnDelay = delayInHours * (int) Date.MILLIS_IN_HOUR;
	}

}
