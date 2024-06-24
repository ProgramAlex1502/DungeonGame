package main.java.org.dungeon.util;

import java.util.Arrays;

public class Dimensions {
	
	private final int[] dimensions;
	
	public Dimensions(int... dimensions) {
		this.dimensions = dimensions;
	}
	
	public boolean equals(Dimensions anotherObject) {
		return Arrays.equals(dimensions, anotherObject.dimensions);
	}

}
