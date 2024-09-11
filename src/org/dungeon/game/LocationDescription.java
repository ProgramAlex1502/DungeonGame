package org.dungeon.game;

import java.awt.Color;
import java.io.Serializable;

public class LocationDescription implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final char symbol;
	private final Color color;
	
	public LocationDescription(char symbol, Color color) {
		this.symbol = symbol;
		this.color = color;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public String toString() {
		return String.format("Symbol: %c\nRGB: %d %d %d", symbol, color.getRed(), color.getGreen(), color.getBlue());
	}

}
