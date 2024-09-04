package org.dungeon.game;

import java.io.Serializable;

import org.dungeon.io.DLogger;

public final class Name implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String singular;
	private final String plural;
	
	private Name(String singular, String plural) {
		this.singular = singular;
		this.plural = plural;
	}
	
	public static Name newInstance(String singular) {
		return newInstance(singular, singular + 's');
	}
	
	public static Name newInstance(String singular, String plural) {
		return new Name(singular, plural);
	}
	
	public String getName() {
		return singular;
	}
	
	public String getQuantifiedName(int quantity, QuantificationMode mode) {
		String name = null;
		if (quantity < 0) {
			DLogger.warning("Called getQuantifiedName with nonpositive quantity.");
		} else if (quantity == 1) {
			name = singular;
		} else {
			name = plural;
		}
		String number;
		if (mode == QuantificationMode.NUMBER) {
			number = String.valueOf(quantity);
		} else {
			number = Numeral.getCorrespondingNumeral(quantity).toString().toLowerCase();
		}
		return number + " " + name;
	}

}
