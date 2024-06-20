package main.java.org.dungeon.game;

import java.io.Serializable;
import java.util.HashMap;

import org.joda.time.DateTime;

import main.java.org.dungeon.counters.CounterMap;

public class World implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//TODO: finish World class
	private final CounterMap<String> spawnCounter;
	
	private final WorldGenerator generator;
	
	private final HashMap<Point, Location> locations;
	final DateTime worldCreationDate;
	
	private DateTime worldDate;

	public World() {
		worldDate = new DateTime(1985, 6, 2, 6, 10);
		worldCreationDate = worldDate.minusHours(6);
		spawnCounter = new CounterMap<String>();
		locations = new HashMap<Point, Location>();
		generator = new WorldGenerator(this);
	}
	
	public CounterMap<String> getSpawnCounter() {
		return spawnCounter;
	}
	
	public WorldGenerator getGenerator() {
		return generator;
	}

	public DateTime getWorldCreationDate() {
		return worldCreationDate;
	}
	
	public DateTime getWorldDate() {
		return worldDate;
	}
	
	public void addLocation(Location locationObject, Point coordinates) {
		locations.put(coordinates, locationObject);
	}
	
	public boolean hasLocation(Point point) {
		return locations.containsKey(point);
	}
	
	public Location getLocation(Point point) {
		if (!hasLocation(point)) {
			generator.expand(point);
		}
		return locations.get(point);
	}
	
	public PartOfDay getPartOfDay() {
		return PartOfDay.getCorrespondingConstants(new DateTime(worldDate));
	}
	
	public void rollDate(int seconds) {
		worldDate = worldDate.plusSeconds(seconds);
	}

}
