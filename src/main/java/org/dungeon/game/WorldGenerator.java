package main.java.org.dungeon.game;

import java.io.Serializable;

import main.java.org.dungeon.io.IO;

public class WorldGenerator implements Serializable {
	private static final long serialVersionUID = 1L;

	private final World world;
	
	private final RiverGenerator riverGenerator;
	
	private int chunkSide;
	private int generatedLocations;
	
	private static final int MIN_CHUNK_SIDE = 1;
	private static final int DEF_CHUNK_SIDE = 5;
	private static final int MAX_CHUNK_SIDE = 50;
	
	private static final int MIN_DIST_RIVER = 6;
	private static final int MAX_DIST_RIVER = 11;
	
	public WorldGenerator(World world) {
		this(world, DEF_CHUNK_SIDE);
	}
	
	public WorldGenerator(World world, int chunkSide) {
		this.world = world;
		this.chunkSide = chunkSide;
		riverGenerator = new RiverGenerator(MIN_DIST_RIVER, MAX_DIST_RIVER);
	}
	
	public int getChunkSide() {
		return chunkSide;
	}
	
	public int setChunkSide(int chunkSide) {
		if (chunkSide < MIN_CHUNK_SIDE) {
			return this.chunkSide = MIN_CHUNK_SIDE;
		} else if (chunkSide > MAX_CHUNK_SIDE) {
			return this.chunkSide = MAX_CHUNK_SIDE;
		} else {
			return this.chunkSide = chunkSide;
		}
	}
	
	private Location createRandomLocation() {
		return new Location(GameData.LOCATION_PRESETS[Engine.RANDOM.nextInt(GameData.LOCATION_PRESETS.length)], world);
	}
	
	private Location createRiverLocation() {
		return new Location(GameData.getRandomRiver(), world);
	}
	
	private Location createBridgeLocation() {
		return new Location(GameData.getRandomBridge(), world);
	}

	public void expand(Point point) {
		riverGenerator.expand(point, chunkSide);
		Point current_point;
		
		int pX = point.getX();
		int pY = point.getY();
		int x_start = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
		int y_start = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
		
		for (int x = x_start; x < x_start + chunkSide; x++) {
			for (int y = y_start; y < y_start + chunkSide; y++) {
				current_point = new Point(x, y);
				if (!world.hasLocation(current_point)) {
					if (riverGenerator.isRiver(current_point)) {
						world.addLocation(createRiverLocation(), current_point);
					} else if (riverGenerator.isBridge(current_point)) {
						world.addLocation(createBridgeLocation(), current_point);
					} else {
						world.addLocation(createRandomLocation(), current_point);
					}
					generatedLocations++;
				}
			}
		}
	}
	
	public void printStatistics() {
		IO.writeKeyValueString("Chunk side", String.valueOf(chunkSide));
		IO.writeKeyValueString("Chunk size", String.valueOf(chunkSide * chunkSide));
		IO.writeKeyValueString("Locations", String.valueOf(generatedLocations));
	}

}
