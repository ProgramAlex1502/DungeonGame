package org.dungeon.map;

import org.dungeon.game.Game;
import org.dungeon.game.Point;
import org.dungeon.game.World;
import org.dungeon.gui.GameWindow;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;

public class WorldMap {
	
	private final WorldMapSymbol[][] matrix;
	private final IterationLimits limits;
	private final String stringRepresentation;
	
	private WorldMap() {
		Point center = Game.getGameState().getHeroPosition();
		int cols = Constants.COLS;
		int rows = GameWindow.ROWS - 1;
		limits = new IterationLimits(center, cols, rows);
		matrix = new WorldMapSymbol[rows][cols];
		stringRepresentation = rows + "x" + cols + "map.";
	}
	
	public static WorldMap makeWorldMap() {
		WorldMap map = new WorldMap();
		World world = Game.getGameState().getWorld();
		Point heroPosition = Game.getGameState().getHeroPosition();
		ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
		
		for (int curY = map.limits.minY; curY >= map.limits.maxY; curY--) {
			for (int curX = map.limits.minX; curX <= map.limits.maxX; curX++) {
				Point currentPosition = new Point(curX, curY);
				if (currentPosition.equals(heroPosition)) {
					map.matrix[map.limits.minY - curY][curX - map.limits.minX] = WorldMapSymbol.getHeroSymbol();
				} else if (explorationStatistics.hasBeenSeen(currentPosition)) {
					WorldMapSymbol symbol = WorldMapSymbol.makeSymbol(world.getLocation(currentPosition));
					map.matrix[map.limits.minY - curY][curX - map.limits.minX] = symbol;
				} else {
					map.matrix[map.limits.minY - curY][curX - map.limits.minX] = WorldMapSymbol.getNotYetGeneratedSymbol();
				}
			}
		}
		
		return map;
	}
	
	public static WorldMap makeDebugWorldMap() {
		WorldMap map = new WorldMap();
		World world = Game.getGameState().getWorld();
		Point heroPosition = Game.getGameState().getHeroPosition();
		
		for (int curY = map.limits.minY; curY >= map.limits.maxY; curY--) {
			for (int curX = map.limits.minX; curX <= map.limits.maxX; curX++) {
				Point currentPosition = new Point(curX, curY);
				if (currentPosition.equals(heroPosition)) {
					map.matrix[map.limits.minY - curY][curX - map.limits.minX] = WorldMapSymbol.getHeroSymbol();
				} else {
					WorldMapSymbol symbol = WorldMapSymbol.makeSymbol(world.getLocation(currentPosition));
					map.matrix[map.limits.minY - curY][curX - map.limits.minX] = symbol;
				}
			}
		}
		
		return map;
	}
	
	WorldMapSymbol[][] getSymbolMatrix() {
		return matrix;
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}

}
