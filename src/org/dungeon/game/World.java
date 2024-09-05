package org.dungeon.game;

import java.io.Serializable;
import java.util.HashMap;

import org.dungeon.creatures.Hero;
import org.dungeon.date.Date;
import org.dungeon.stats.WorldStatistics;

public class World implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final WorldGenerator generator;
	
	private final HashMap<Point, Location> locations;
	private final Date worldCreationDate;	
	
	private final WorldStatistics worldStatistics;
	private Date worldDate;
	
	public World(WorldStatistics statistics) {
		worldStatistics = statistics;
		worldDate = new Date(455, 6, 2, 6, 10, 0);
		worldCreationDate = worldDate.minusHours(6);
		locations = new HashMap<Point, Location>();
		generator = new WorldGenerator(this);
	}
	
	public Date getWorldCreationDate() {
		return worldCreationDate;
	}
	
	public Date getWorldDate() {
		return worldDate;
	}
	
	public void addLocation(Location locationObject, Point coordinates) {
		locations.put(coordinates, locationObject);
		worldStatistics.addLocation(locationObject.getName().getSingular());
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
		return PartOfDay.getCorrespondingConstant(worldDate);
	}
	
	public void rollDate(int seconds) {
		worldDate = worldDate.plusSeconds(seconds);
	}

}
