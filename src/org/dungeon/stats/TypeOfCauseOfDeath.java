package org.dungeon.stats;

import java.io.Serializable;

public enum TypeOfCauseOfDeath implements Serializable {
	
	UNARMED("Unarmed"), WEAPON("Weapon"), SKILL("Skill");
	
	private final String stringRepresentation;
	
	TypeOfCauseOfDeath(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}

}
