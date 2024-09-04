package org.dungeon.creatures;

public enum HealthState {
	
	UNINJURED("Uninjured"),
	BARELY_UNINJURED("Barely Injured"),
	INJURED("Injured"),
	BADLE_INJURED("Badly Injured"),
	NEAR_DEATH("Nead Death"),
	DEAD("Dead");
	
	private final String stringRepresentation;
	
	HealthState(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	public static HealthState getHealthState(int curHealth, int maxHealth) {
		double fraction = curHealth / (double) maxHealth;
		return values()[(int) ((values().length - 1) *( 1 - fraction))];
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}

}
