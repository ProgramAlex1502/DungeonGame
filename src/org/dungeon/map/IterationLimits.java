package org.dungeon.map;

import org.dungeon.game.Point;

class IterationLimits {
	
	final int minX;
	final int maxX;
	final int minY;
	final int maxY;
	
	IterationLimits(Point center, int cols, int rows) {
		minX = center.getX() - (cols - 1) / 2;
		maxX = minX + cols - 1;
		minY = center.getY() + (rows - 1) / 2;
		maxY = minY - rows + 1;
	}

}
