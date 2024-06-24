package main.java.org.dungeon.game;

import java.io.Serializable;
import java.util.HashMap;

import org.joda.time.DateTime;

import main.java.org.dungeon.counters.CounterMap;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.io.IO;

public class World implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<ID> spawnCounter;
	
	private final WorldGenerator generator;
	
	private final HashMap<Point, Location> locations;
	final DateTime worldCreationDate;
	private DateTime worldDate;

	public World() {
		worldDate = new DateTime(1985, 6, 2, 6, 10);
		worldCreationDate = worldDate.minusHours(6);
		spawnCounter = new CounterMap<ID>();
		locations = new HashMap<Point, Location>();
		generator = new WorldGenerator(this);
	}
	
	public CounterMap<ID> getSpawnCounter() {
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
	
	public Location moveHero(Direction dir) {
		Hero hero = Game.getGameState().getHero();
		Point heroOldPosition = Game.getGameState().getHeroPosition();
		Point heroNewPosition = new Point(heroOldPosition, dir);
		Game.getGameState().setHeroPosition(heroNewPosition);
		locations.get(heroOldPosition).removeCreature(hero);
		Location heroNewLocation = locations.get(heroNewPosition);
		heroNewLocation.addCreature(hero);
		return heroNewLocation;
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
	
	public void printSpawnCounters() {
		for (ID id : spawnCounter.keySet()) {
			IO.writeKeyValueString(id.getId(), Integer.toString(spawnCounter.getCounter(id)));
		}
	}
	
	public void rollDate(int seconds) {
		worldDate = worldDate.plusSeconds(seconds);
	}

}
