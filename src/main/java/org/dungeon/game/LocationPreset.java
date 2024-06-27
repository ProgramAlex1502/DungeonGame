package main.java.org.dungeon.game;

import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.util.Percentage;

public final class LocationPreset extends Entity {
	private static final long serialVersionUID = 1L;
	
	private final BlockedEntrances blockedEntrances = new BlockedEntrances();
	private final ArrayList<SpawnerPreset> spawners = new ArrayList<SpawnerPreset>();
	private final ArrayList<ItemFrequencyPair> items = new ArrayList<ItemFrequencyPair>();
	private Percentage lightPermittivity;
	private int blobSize;
	
	LocationPreset(String id, String type, String name) {
		super(new ID(id), type, name);
	}
	
	public List<SpawnerPreset> getSpawners() {
		return spawners;
	}
	

	public LocationPreset addSpawner(SpawnerPreset spawner) {
		this.spawners.add(spawner);
		
		return this;
	}

	public List<ItemFrequencyPair> getItems() {
		return items;
	}
	
	public LocationPreset addItem(String id, Double likelihood) {
		this.items.add(new ItemFrequencyPair(new ID(id), likelihood));
		
		return this;
	}
	
	public BlockedEntrances getBlockedEntrances() {
		return blockedEntrances;
	}

	public LocationPreset block(Direction direction) {
		blockedEntrances.block(direction);
		
		return this;
	}
	
	public Percentage getLightPermittivity() {
		return lightPermittivity;
	}

	public void setLightPermittivity(double lightPermittivity) {
		this.lightPermittivity = new Percentage(lightPermittivity);
	}
	
	public int getBlobSize() {
		return blobSize;
	}
	
	public void setBlobSize(int blobSize) {
		this.blobSize = blobSize;
	}
	
	void finish() {
		spawners.trimToSize();
		items.trimToSize();
	}

}
