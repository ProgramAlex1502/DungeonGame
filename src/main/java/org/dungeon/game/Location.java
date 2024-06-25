package main.java.org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.items.ItemBlueprint;
import main.java.org.dungeon.items.LocationInventory;
import main.java.org.dungeon.util.Percentage;

public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ID id;
	private final String name;
	private final BlockedEntrances blockedEntrances;
	private final List<Creature> creatures;
	private final List<Spawner> spawners;
	private final LocationInventory items;
	
	private final Percentage lightPermittivity;
	private World world;
	
	public Location(LocationPreset preset, World world) {
		this.id = preset.id;
		this.world = world;
		this.name = preset.getName();
		this.blockedEntrances = preset.getBlockedEntrances();
		this.lightPermittivity = preset.getLightPermittivity();
		this.creatures = new ArrayList<Creature>();
		this.spawners = new ArrayList<Spawner>(preset.getSpawners().size());
		
		for (SpawnerPreset spawner : preset.getSpawners()) {
			spawners.add(new Spawner(spawner, this));
		}
		
		this.items = new LocationInventory();
		
		for (ItemFrequencyPair pair : preset.getItems()) {
			if (Engine.RANDOM.nextDouble() < pair.getFrequency()) {
				ItemBlueprint blueprint = GameData.getItemBlueprints().get(pair.getID());
				if (blueprint != null) {
					this.addItem(new Item(blueprint));
				} else {
					DLogger.warning("ItemBlueprint not found: " + pair.getID().toString());
				}
			}
		}
		
	}
	
	public ID getID() {
		return id;
	}
	
	public void refreshSpawners() {
		for (Spawner spawner : spawners) {
			spawner.refresh();
		}
	}
	
	public String getName() {
		return name;
	}
	
	Percentage getLightPermittivity() {
		return lightPermittivity;
	}
	
	public double getLuminosity() {
		return getLightPermittivity().toDouble() * getWorld().getPartOfDay().getLuminosity();
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
		world.getWorldStatistics().addSpawn(creature.getName());
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
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public boolean isBlocked(Direction direction) {
		return blockedEntrances.isBlocked(direction);
	}
}
