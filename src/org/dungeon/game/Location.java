package org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.dungeon.creatures.Creature;
import org.dungeon.io.DLogger;
import org.dungeon.items.Item;
import org.dungeon.items.ItemFactory;
import org.dungeon.items.LocationInventory;
import org.dungeon.util.Percentage;

public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	private final ID id;
	private final String type;
	private final String name;
	private final BlockedEntrances blockedEntrances;
	private final List<Creature> creatures;
	private final List<Spawner> spawners;
	private final LocationInventory items;
	
	private final Percentage lightPermittivity;
	private final World world;
	
	public Location(LocationPreset preset, World world) {
		this.id = preset.getID();
		this.type = preset.getType();
		this.name = preset.getName();
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
			if (Engine.roll(entry.getValue())) {
				Item item = ItemFactory.makeItem(entry.getKey(), world.getWorldDate());
				if (item != null) {
					this.addItem(item);
				} else {
					DLogger.warning("ItemBlueprint not found: " + entry.getKey().toString());
				}
			}
		}
	}
	
	public ID getID() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
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
	
	public int getItemCount() {
		return items.getItems().size();
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

}
