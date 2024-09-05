package org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dungeon.util.Percentage;

public final class LocationPreset implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ID id;
	private final String type;
	private final Name name;
	private final BlockedEntrances blockedEntrances = new BlockedEntrances();
	private final List<SpawnerPreset> spawners = new ArrayList<SpawnerPreset>();
	private final Map<ID, Percentage> items = new HashMap<ID, Percentage>();
	private Percentage lightPermittivity;
	private int blobSize;
	
	LocationPreset(ID id, String type, Name name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}
	
	public ID getID() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public Name getName() {
		return name;
	}
	
	public List<SpawnerPreset> getSpawners() {
		return spawners;
	}
	
	public LocationPreset addSpawner(SpawnerPreset spawner) {
		this.spawners.add(spawner);
		return this;
	}
	
	public Set<Entry<ID, Percentage>> getItems() {
		return items.entrySet();
	}
	
	public LocationPreset addItem(String id, Double likelihood) {
		items.put(new ID(id), new Percentage(likelihood));
		return this;
	}
	
	public BlockedEntrances getBlockedEntrances() {
		return new BlockedEntrances(blockedEntrances);
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
}
