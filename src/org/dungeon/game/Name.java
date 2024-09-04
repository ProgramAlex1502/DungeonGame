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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		Name name = (Name) o;
		
		if (singular != null ? !singular.equals(name.singular) : name.singular != null) {
			return false;
		}
		if (plural != null ? !plural.equals(name.plural) : name.plural != null) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = singular != null ? singular.hashCode() : 0;
		result = 31 * result + (plural != null ? plural.hashCode() : 0);
		return result;
	}

}
