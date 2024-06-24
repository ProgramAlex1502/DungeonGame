package main.java.org.dungeon.game;

import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.util.Percentage;

final class LocationPreset extends Preset {
		
	private final String name;
	private final BlockedEntrances blockedEntrances;
	private final ArrayList<SpawnerPreset> spawners;
	private final ArrayList<ItemFrequencyPair> items;
	private Percentage lightPermittivity;
	
	LocationPreset(String name) {
		this.name = name;
		blockedEntrances = new BlockedEntrances();
		spawners = new ArrayList<SpawnerPreset>();
		items = new ArrayList<ItemFrequencyPair>();
	}
	
	public LocationPreset addSpawner(SpawnerPreset spawner) {
		if (!isLocked()) {
			this.spawners.add(spawner);
		}
		
		return this;
	}
	
	public LocationPreset addItem(String id, Double likelihood) {
		if (!isLocked()) {
			this.items.add(new ItemFrequencyPair(new ID(id), likelihood));
		}
		
		return this;
	}
	
	public void setLightPermittivity(double lightPermittivity) {
		if (!isLocked()) {
			this.lightPermittivity = new Percentage(lightPermittivity);
		}
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
	
	public Percentage getLightPermittivity() {
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
