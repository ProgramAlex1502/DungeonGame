package main.java.org.dungeon.creatures;

public enum HealthState {
	
	UNINJURED("Uninjured"),
	BARELY_INJURED("Barely Injured"),
	INJURED("Injured"),
	BADLY_INJURED("Badly Injured"),
	NEAR_DEATH("Nead Death"),
	DEAD("Dead");
	
	private final String stringRepresentation;
	
	HealthState(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	public static HealthState getHealthState(int curHealth, int maxHealth) {
		double fraction = curHealth / (double) maxHealth;
		return values()[(int) (5 * (1 - fraction))];
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}

}
