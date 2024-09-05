package org.dungeon.game;

import java.io.Serializable;
import java.util.HashMap;

final class RiverGenerator implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private static final int MIN_BRIDGE_DIST = 6;
	private static final int MAX_BRIDGE_DIST = 16;
	private static final int START = 10;
	
	private final ExpandableIntegerSet lines;
	private final HashMap<Integer, River> rivers;
	
	public RiverGenerator(int minimumDistance, int maximumDistance) {
		lines = new ExpandableIntegerSet(minimumDistance, maximumDistance);
		rivers = new HashMap<Integer, River>();
	}
	
	void expand(Point point, int chunkSide) {
		for (int river : lines.expand(point.getX() - chunkSide)) {
			if (river <= -START) {
				rivers.put(river, new River(MIN_BRIDGE_DIST, MAX_BRIDGE_DIST));
			}
		}
		for (int river : lines.expand(point.getX() + chunkSide)) {
			if (river >= START) {
				rivers.put(river, new River(MIN_BRIDGE_DIST, MAX_BRIDGE_DIST));
			}
		}
	}
	
	boolean isRiver(Point point) {
		River river = rivers.get(point.getX());
		return river != null && !river.isBridge(point.getY());
	}
	
	boolean isBridge(Point point) {
		River river = rivers.get(point.getX());
		return river != null && river.isBridge(point.getY());
	}

}
