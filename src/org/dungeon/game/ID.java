package org.dungeon.game;

import java.io.Serializable;

import org.dungeon.io.DLogger;

public final class ID implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	
	public ID(String id) {
		if (id == null) {
			DLogger.warning("Tried to use a null String as an ID.");
			this.id = "null";
			return;
		} else {
			boolean invalid = false;
			char[] idChars = id.toCharArray();
			char currentChar;
			for (int i = 0; i < idChars.length; i++) {
				currentChar = idChars[i];
				if (Character.isLetter(currentChar)) {
					if (Character.isLowerCase(idChars[i])) {
						invalid = true;
						idChars[i] = Character.toUpperCase(currentChar);
					}
				} else if (!(Character.isDigit(currentChar) || idChars[i] == '_')) {
					invalid = true;
					idChars[i] = '_';
				}
			}
			if (invalid) {
				DLogger.warning("Tried to use \"" + id + "\" as an ID.");
			}
			this.id = new String(idChars);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		ID oid = (ID) o;
		
		return id.equals(oid.id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return id;
	}

}
