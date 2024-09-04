package org.dungeon.stats;

import java.io.Serializable;

public enum TypeOfCauseOfDeath implements Serializable {
	
	WEAPON("Weapon"), SKILL("Skill");
	
	public final String stringRepresentation;
	
	TypeOfCauseOfDeath(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}

}
