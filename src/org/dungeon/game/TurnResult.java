package org.dungeon.game;

class TurnResult {
	
	int turnLength;
	boolean configurationsChanged;
	
	void clear() {
		turnLength = 0;
		configurationsChanged = false;
	}
	
	boolean gameStateChanged() {
		return turnLength != 0 || configurationsChanged;
	}

}
