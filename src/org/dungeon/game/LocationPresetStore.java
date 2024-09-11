package org.dungeon.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dungeon.game.LocationPreset.Type;

public class LocationPresetStore {
	
	private final Map<ID, LocationPreset> idLocationPresetMap = new HashMap<ID, LocationPreset>();
	private final Map<Type, List<LocationPreset>> typeLocationPresetMap = new HashMap<Type, List<LocationPreset>>();
	
	LocationPresetStore() {
	}
	
	public void addLocationPreset(LocationPreset preset) {
		idLocationPresetMap.put(preset.getID(), preset);
		if (!typeLocationPresetMap.containsKey(preset.getType())) {
			typeLocationPresetMap.put(preset.getType(), new ArrayList<LocationPreset>());
		}
		typeLocationPresetMap.get(preset.getType()).add(preset);
	}
	
	public Collection<LocationPreset> getAllPresets() {
		return idLocationPresetMap.values();
	}
	
	List<LocationPreset> getLocationPresetByType(Type type) {
		return typeLocationPresetMap.get(type);
	}
	
	public int getSize() {
		return idLocationPresetMap.size();
	}
	
	public String toString() {
		Set<Type> types = typeLocationPresetMap.keySet();
		return String.format("LocationPresetStore with %d presets of the following types %s.", getSize(), types);
	}

}
