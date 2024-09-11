package org.dungeon.game;

import java.util.List;

import org.dungeon.util.Percentage;

public class Random {
	
	private static final java.util.Random RANDOM = new java.util.Random();
	
	public static boolean roll(Percentage chance) {
		return chance.toDouble() > RANDOM.nextDouble();
	}
	
	public static boolean roll(double chance) {
		return roll(new Percentage(chance));
	}
	
	public static boolean nextBoolean() {
		return RANDOM.nextBoolean();
	}
	
	public static int nextInteger(int n) {
		return RANDOM.nextInt(n);
	}
	
	public static <T> T select(List<T> list) {
		if (list.isEmpty()) {
			throw new IllegalArgumentException("list is empty.");
		}
		return list.get(nextInteger(list.size()));
	}

}
