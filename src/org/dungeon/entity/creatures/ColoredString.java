package org.dungeon.entity.creatures;

import java.awt.Color;

final class ColoredString {
	
	private final String string;
	private final Color color;
	
	public ColoredString(String string, Color color) {
		this.string = string;
		this.color = color;
	}
	
	public String getString() {
		return string;
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		ColoredString that = (ColoredString) o;
		return string.equals(that.string) && color.equals(that.color);
	}
	
	@Override
	public int hashCode() {
		return 31 * string.hashCode() + (color.hashCode());
	}
	
	@Override
	public String toString() {
		return String.format("'%s' written in %s.", string, color);
	}

}
