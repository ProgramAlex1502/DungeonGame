package main.java.org.dungeon.game;

import java.util.ArrayList;
import java.util.List;

final class LocationPreset extends Preset {
	
	//TODO: finish LocationPreset class
	
	private String name;
	private ArrayList<SpawnerPreset> spawners;
	private double lightPermittivity;
	
	LocationPreset(String name) {
		this.name = name;
		
		spawners = new ArrayList<SpawnerPreset>();
	}
	
	public String getName() {
		return name;
	}
	
	public List<SpawnerPreset> getSpawners() {
		return spawners;
	}
	
	public double getLightPermittivity() {
		return lightPermittivity;
	}

}
