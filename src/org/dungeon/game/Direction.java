package org.dungeon.game;

public enum Direction {
	
	NORTH("North", "N", 0, 1),
	EAST("East", "E", 1, 0),
	SOUTH("South", "S", 0, -1),
	WEST("West", "W", -1, 0);
	
	private final String name;
	private final String abbreviation;
	private final int x;
	private final int y;
	
	Direction(String name, String abbreviation, int x, int y) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.x = x;
		this.y = y;
	}
	
	public static Direction fromAbbreviation(String abbreviation) {
		for (Direction direction : values()) {
			if (direction.abbreviation.equals(abbreviation)) {
				return direction;
			}
		}
		return null;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Direction invert() {
		return values()[(ordinal() + values().length / 2) % values().length];
	}
	
	public boolean equalsIgnoreCase(String str) {
		return name.equalsIgnoreCase(str) || abbreviation.equalsIgnoreCase(str);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
