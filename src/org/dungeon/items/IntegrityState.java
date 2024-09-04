package org.dungeon.items;

public enum IntegrityState {
	
	PERFECT(""),
	SLIGHTLY_DAMAGED("Slightly Damaged"),
	DAMAGED("Damaged"),
	SEVERELY_DAMAGED("Severely Damaged"),
	BROKEN("Broken");
	
	private final String stringRepresentation;
	
	IntegrityState(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	public static IntegrityState getIntegrityState(int curIntegrity, int maxIntegrity) {
		if (curIntegrity == maxIntegrity) {
			return PERFECT;
		} else if (curIntegrity >= maxIntegrity * 0.65) {
			return SLIGHTLY_DAMAGED;
		} else if (curIntegrity >= maxIntegrity * 0.3) {
			return DAMAGED;
		} else if (curIntegrity > 0) {
			return SEVERELY_DAMAGED;
		} else {
			return BROKEN;
		}
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}

}
