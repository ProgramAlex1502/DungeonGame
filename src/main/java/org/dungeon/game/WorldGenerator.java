package main.java.org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorldGenerator implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private static final int MIN_CHUNK_SIDE = 1;
	private static final int DEF_CHUNK_SIDE = 5;
	private static final int MAX_CHUNK_SIDE = 50;
	
	private static final int MIN_DIST_RIVER = 6;
	private static final int MAX_DIST_RIVER = 11;

	private final World world;
	private final RiverGenerator riverGenerator;
	private int chunkSide;
	
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
	
	private LocationPreset getRandomLandLocation() {
		List<LocationPreset> locationPresets = new ArrayList<LocationPreset>(GameData.getLocationPresets().values());
		LocationPreset selectedPreset;
		do {
			selectedPreset = locationPresets.get(Engine.RANDOM.nextInt(locationPresets.size()));
		} while (!"Land".equals(selectedPreset.getType()));
		return selectedPreset;
	}
	
	private Location createRiverLocation() {
		return new Location(GameData.getLocationPresets().get(new ID("RIVER")), world);
	}
	
	private Location createBridgeLocation() {
		return new Location(GameData.getLocationPresets().get(new ID("BRIDGE")), world);
	}

	public void expand(Point point) {
		riverGenerator.expand(point, chunkSide);
		Point currentPoint;
		
		LocationPreset currentLocationPreset = null;
		int remainingLocationsOfCurrentPreset = 0;
		
		int pX = point.getX();
		int pY = point.getY();
		int x_start = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
		int y_start = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
		
		for (int x = x_start; x < x_start + chunkSide; x++) {
			for (int y = y_start; y < y_start + chunkSide; y++) {
				currentPoint = new Point(x, y);
				if (!world.hasLocation(currentPoint)) {
					if (riverGenerator.isRiver(currentPoint)) {
						world.addLocation(createRiverLocation(), currentPoint);
					} else if (riverGenerator.isBridge(currentPoint)) {
						world.addLocation(createBridgeLocation(), currentPoint);
					} else {
						if (currentLocationPreset == null || remainingLocationsOfCurrentPreset == 0) {
							currentLocationPreset = getRandomLandLocation();
							remainingLocationsOfCurrentPreset = currentLocationPreset.getBlobSize();
						}
						world.addLocation(new Location(currentLocationPreset, world), currentPoint);
						remainingLocationsOfCurrentPreset--;
					}
				}
			}
		}
	}
	
}
