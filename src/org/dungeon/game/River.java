package org.dungeon.game;

import java.io.Serializable;

class River implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ExpandableIntegerSet bridges;
	
	River(int MIN_DBB, int MAX_DBB) {
		bridges = new ExpandableIntegerSet(MIN_DBB, MAX_DBB);
	}
	
	private void expand(int y) {
		bridges.expand(y);
	}
	
	boolean isBridge(int y) {
		expand(y);
		return bridges.contains(y);
	}

}
