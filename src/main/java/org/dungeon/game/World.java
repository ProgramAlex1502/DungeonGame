package main.java.org.dungeon.game;

import java.io.Serializable;
import java.util.HashMap;

import org.joda.time.DateTime;

public class World implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//TODO: finish World class
	
	private final WorldGenerator generator;
	
	private final HashMap<Point, Location> locations;
	
	public World() {
		locations = new HashMap<Point, Location>();
		generator = new WorldGenerator(this);
	}

	private DateTime worldDate;
	
	public DateTime getWorldDate() {
		return worldDate;
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

}
