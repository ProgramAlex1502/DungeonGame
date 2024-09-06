package org.dungeon.game;

import org.dungeon.gui.GameWindow;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;

class WorldMap {

	private static final char HERO_SYMBOL = '@';
	private static final char NOT_YET_SEEN_SYMBOL = '?';
	private static final char NOT_YET_GENERATED_SYMBOL = '~';
	
	private final String map;
	
	public WorldMap(World world, ExplorationStatistics explorationStatistics, Point heroPosition) {
		int rows = GameWindow.ROWS - 1;
		int cols = Constants.COLS;
		int initX = heroPosition.getX() - (cols - 1) / 2;
		int lastX = initX + cols - 1;
		int initY = heroPosition.getY() + (rows - 1) / 2;
		int lastY = initY - rows + 1;
		StringBuilder builder = new StringBuilder((cols + 1) * rows);
		for (int curY = initY; curY >= lastY; curY--) {
			for (int curX = initX; curX <= lastX; curX++) {
				Point currentPosition = new Point(curX, curY);
				if (currentPosition.equals(heroPosition)) {
					builder.append(HERO_SYMBOL);
				} else if (world.hasLocation(currentPosition)) {
					if (explorationStatistics.hasBeenSeen(currentPosition)) {
						builder.append(world.getLocation(currentPosition).getName().getSingular().charAt(0));						
					} else {
						builder.append(NOT_YET_SEEN_SYMBOL);
					}
				} else {
					builder.append(NOT_YET_GENERATED_SYMBOL);
				}
			}
			builder.append('\n');
		}
		map = builder.toString();
	}
	
	@Override
	public String toString() {
		return map;
	}

}
