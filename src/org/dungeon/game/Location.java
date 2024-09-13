package org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.entity.items.LocationInventory;
import org.dungeon.io.DLogger;
import org.dungeon.util.Percentage;

public final class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	private final ID id;
	private final Name name;
	private final LocationDescription description;
	private final BlockedEntrances blockedEntrances;
	private final List<Creature> creatures;
	private final List<Spawner> spawners;
	private final LocationInventory items;
	
	private final Percentage lightPermittivity;
	private final World world;
	
	public Location(LocationPreset preset, World world) {
		this.id = preset.getID();
		this.name = preset.getName();
		this.description = preset.getDescription();
		this.world = world;
		this.blockedEntrances = preset.getBlockedEntrances();
		this.lightPermittivity = preset.getLightPermittivity();
		this.creatures = new ArrayList<Creature>();
		this.spawners = new ArrayList<Spawner>(preset.getSpawners().size());
		for (SpawnerPreset spawner : preset.getSpawners()) {
			spawners.add(new Spawner(spawner, this));
		}
		this.items = new LocationInventory();
		for (Entry<ID, Percentage> entry : preset.getItems()) {
			if (Random.roll(entry.getValue())) {
				Item item = ItemFactory.makeItem(entry.getKey(), world.getWorldDate());
				if (item != null) {
					this.addItem(item);
				} else {
					DLogger.severe("ItemBlueprint not found: " + entry.getKey().toString());
				}
			}
		}
	}
	
	public ID getID() {
		return id;
	}
	
	public Name getName() {
		return name;
	}
	
	public LocationDescription getDescription() {
		return description;
	}
	
	public void refreshSpawners() {
		for (Spawner spawner : spawners) {
			spawner.refresh();
		}
	}
	
	public Percentage getLightPermittivity() {
		return lightPermittivity;
	}
	
	public Percentage getLuminosity() {
		return getLightPermittivity().multiply(getWorld().getPartOfDay().getLuminosity());
	}
	
	public List<Creature> getCreatures() {
		return creatures;
	}
	
	public LocationInventory getInventory() {
		return items;
	}
	
	public List<Item> getItemList() {
		return items.getItems();
	}
	
	public int getCreatureCount() {
		return creatures.size();
	}
	
	public int getCreatureCount(ID id) {
		int count = 0;
		for (Creature creature : creatures) {
			if (creature.getID().equals(id)) {
				count++;
			}
		}
		return count;
	}
	
	public void addCreature(Creature creature) {
		creature.setLocation(this);
		creatures.add(creature);
	}
	
	public void addItem(Item item) {
		items.addItem(item);
	}
	
	public void removeItem(Item item) {
		items.removeItem(item);
	}
	
	public void removeCreature(Creature creature) {
		for (Spawner spawner : spawners) {
			spawner.notifyKill(creature);
		}
		
		creatures.remove(creature);
	}
	
	public World getWorld() {
		return world;
	}
	
	public BlockedEntrances getBlockedEntrances() {
		return blockedEntrances;
	}
	
	public boolean isBlocked(Direction direction) {
		return blockedEntrances.isBlocked(direction);
	}
	
	@Override
	public String toString() {
		return name.getSingular();
	}

}
