package org.dungeon.game;

import java.io.Serializable;

import org.dungeon.game.LocationPreset.Type;

class WorldGenerator implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int CHUNK_SIDE = 5;
	
	private static final int MIN_DIST_RIVER = 6;
	private static final int MAX_DIST_RIVER = 11;
	private final World world;
	private final RiverGenerator riverGenerator;
	private final int chunkSide;
	
	public WorldGenerator(World world) {
		this.world = world;
		this.chunkSide = WorldGenerator.CHUNK_SIDE;
		riverGenerator = new RiverGenerator(MIN_DIST_RIVER, MAX_DIST_RIVER);
	}
	
	private static LocationPreset getRandomLandLocationPreset() {
		return Random.select(GameData.getLocationPresetStore().getLocationPresetByType(Type.LAND));
	}
	
	private Location createRandomRiverLocation() {
		return new Location(Random.select(GameData.getLocationPresetStore().getLocationPresetByType(Type.RIVER)), world);
	}
	
	private Location createRandomBridgeLocation() {
		return new Location(Random.select(GameData.getLocationPresetStore().getLocationPresetByType(Type.BRIDGE)), world);
	}
	
	public void expand(Point p) {
		riverGenerator.expand(p, chunkSide);
		Point currentPoint;
		LocationPreset currentLocationPreset = null;
		int remainingLocationsOfCurrentPreset = 0;
		
		int pX = p.getX();
        int pY = p.getY();
        int startX = pX < 0 ? chunkSide * (((pX + 1) / chunkSide) - 1) : chunkSide * (pX / chunkSide);
        int startY = pY < 0 ? chunkSide * (((pY + 1) / chunkSide) - 1) : chunkSide * (pY / chunkSide);
        for (int x = startX; x < startX + chunkSide; x++) {
            for (int y = startY; y < startY + chunkSide; y++) {
                currentPoint = new Point(x, y);
                if (!world.hasLocation(currentPoint)) {
                    if (riverGenerator.isRiver(currentPoint)) {
                        world.addLocation(createRandomRiverLocation(), currentPoint);
                    } else if (riverGenerator.isBridge(currentPoint)) {
                        world.addLocation(createRandomBridgeLocation(), currentPoint);
                    } else {
                    	if (currentLocationPreset == null || remainingLocationsOfCurrentPreset == 0) {
                    		currentLocationPreset = getRandomLandLocationPreset();
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
