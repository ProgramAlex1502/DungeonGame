package main.java.org.dungeon.game;

import java.util.ArrayList;
import java.util.List;

final class LocationPreset extends Preset {
		
	private String name;
	private BlockedEntrances blockedEntrances;
	private ArrayList<SpawnerPreset> spawners;
	private ArrayList<ItemFrequencyPair> items;
	private double lightPermittivity;
	
	LocationPreset(String name) {
		this.name = name;
		blockedEntrances = new BlockedEntrances();
		spawners = new ArrayList<SpawnerPreset>();
		items = new ArrayList<ItemFrequencyPair>();
	}
	
	public LocationPreset addPreset(SpawnerPreset spawner) {
		if (!isLocked()) {
			this.spawners.add(spawner);
		}
		
		return this;
	}
	
	public LocationPreset addItem(String id, Double likelihood) {
		if (!isLocked()) {
			this.items.add(new ItemFrequencyPair(id, likelihood));
		}
		
		return this;
	}
	
	public LocationPreset setLightPermittivity(double lightPermittivity) {
		if (!isLocked()) {
			this.lightPermittivity = lightPermittivity;
		}
		return this;
	}
	
	public LocationPreset block(Direction direction) {
		if (!isLocked()) {
			blockedEntrances.block(direction);
		}
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public BlockedEntrances getBlockedEntrances() {
		return blockedEntrances;
	}
	
	public List<SpawnerPreset> getSpawners() {
		return spawners;
	}
	
	public List<ItemFrequencyPair> getItems() {
		return items;
	}
	
	public double getLightPermittivity() {
		return lightPermittivity;
	}
	
	void finish() {
		if (!isLocked()) {
			spawners.trimToSize();
			items.trimToSize();
			lock();
		}
	}

}
