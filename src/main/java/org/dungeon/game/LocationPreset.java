package main.java.org.dungeon.game;

import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.util.Percentage;

final class LocationPreset extends Entity {
	private static final long serialVersionUID = 1L;
	
	private final BlockedEntrances blockedEntrances = new BlockedEntrances();
	private final ArrayList<SpawnerPreset> spawners = new ArrayList<SpawnerPreset>();
	private final ArrayList<ItemFrequencyPair> items = new ArrayList<ItemFrequencyPair>();
	private Percentage lightPermittivity;
	
	LocationPreset(String id, String type, String name) {
		super(new ID(id), type, name);
	}
	
	public LocationPreset addSpawner(SpawnerPreset spawner) {
		this.spawners.add(spawner);
		
		return this;
	}
	
	public LocationPreset addItem(String id, Double likelihood) {
		this.items.add(new ItemFrequencyPair(new ID(id), likelihood));
		
		return this;
	}
	
	public LocationPreset block(Direction direction) {
		blockedEntrances.block(direction);
		
		return this;
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

	public LocationPreset setLightPermittivity(double lightPermittivity) {
		this.lightPermittivity = new Percentage(lightPermittivity);
		
		return this;
	}
	
	void finish() {
		spawners.trimToSize();
		items.trimToSize();
	}

}
