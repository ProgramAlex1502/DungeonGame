package org.dungeon.game;

import org.dungeon.io.DLogger;

public enum Numeral {
	
	ONE("One"), TWO("Two"), THREE("Three"), FOUR("Four"), FIVE("Five"), MORE_THAN_FIVE("A few");
	
	final String stringRepresentation;
	
	Numeral(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	public static Numeral getCorrespondingNumeral(int integer) {
		if (integer < 1) {
			DLogger.warning("Tried to get nonpositive numeral!");
			return null;
		} else if (integer >= values().length) {
			return values()[values().length - 1];
		} else {
			return values()[integer - 1];
		}
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}

}
