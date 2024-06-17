package main.java.org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.items.LocationInventory;

public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//TODO: finish Location class

	private World world;
	
	private final String name;
	
	private final List<Creature> creatures;
	
	private final LocationInventory items;
	
	private final double lightPermittivity;
	
	public Location(LocationPreset preset, World world) {
		this.world = world;
		this.name = preset.getName();
		this.lightPermittivity = preset.getLightPermittivity();
		this.creatures = new ArrayList<Creature>();
		
		this.items = new LocationInventory();
	}
	
	public String getName() {
		return name;
	}
	
	double getLightPermittivity() {
		return lightPermittivity;
	}
	
	public double getLuminosity() {
		return getLightPermittivity() * getWorld().getPartOfDay().getLuminosity();
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
	
	public int getCreatureCount(String id) {
		int count = 0;
		
		for (Creature creature : creatures) {
			if (creature.getId().equals(id)) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getItemCount() {
		return items.getItems().size();
	}
	
	public World getWorld() {
		return world;
	}
}
