package org.dungeon.items;

import java.io.Serializable;

public class FoodComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int nutrition;
	private final int integrityDecrementOnEat;
	
	public FoodComponent(int nutrition, int integrityDecrementOnEat) {
		this.nutrition = nutrition;
		this.integrityDecrementOnEat = integrityDecrementOnEat;
	}
	
	public int getNutrition() {
		return nutrition;
	}
	
	public int getIntegrityDecrementOnEat() {
		return integrityDecrementOnEat;
	}

}
