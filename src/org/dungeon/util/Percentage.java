package org.dungeon.util;

import java.io.Serializable;
import java.util.Locale;

import org.dungeon.io.DLogger;

public class Percentage implements Comparable<Percentage>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final double ONE = 1.0;
	private static final double ZERO = 0.0;
	
	private final double value;
	
	public Percentage(double percentage) {
		if (DungeonMath.fuzzyCompare(percentage, ZERO) < 0) {
			value = ZERO;
			DLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ZERO + " instead.");
		} else if (DungeonMath.fuzzyCompare(percentage, ONE) > 0) {
			value = ONE;
			DLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ONE + " instead.");
		} else {
			value = percentage;
		}
	}
	
	public static Percentage fromString(String percentage) {
		if (!isValidPercentageString(percentage)) {
			throw new IllegalArgumentException("Provided String is not a valid percentage: " + percentage + "!");
		}
		return new Percentage(doubleFromPercentageString(percentage));
	}
	
	public static boolean isValidPercentageString(String percentage) {
		if (percentage != null) {
			try {
				return isValidPercentageDouble(doubleFromPercentageString(percentage));
			} catch (NumberFormatException e) {
			}
		}
		return false;
	}
	
	private static double doubleFromPercentageString(String percentage) {
		return Double.parseDouble(trimAndDiscardLastCharacter(percentage)) / 100;
	}
	
	private static boolean isValidPercentageDouble(double value) {
		return DungeonMath.fuzzyCompare(value, ZERO) >= 0 && DungeonMath.fuzzyCompare(value, ONE) <= 0;
	}
	
	private static String trimAndDiscardLastCharacter(String string) {
		String trimmed = string.trim();
		return trimmed.substring(0, trimmed.length() - 1);
	}
	
	public double toDouble() {
		return value;
	}
	
	public Percentage multiply(Percentage o) {
		return new Percentage(toDouble() * o.toDouble());
	}
	
	@Override
	public int compareTo(Percentage o) {
		return DungeonMath.fuzzyCompare(toDouble(), o.toDouble());
	}
	
	public boolean biggerThanOrEqualTo(Percentage o) {
		return compareTo(o) >= 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return compareTo((Percentage) o) == 0;
	}
	
	@Override
	public int hashCode() {
		long temp = Double.doubleToLongBits(value);
		return (int) (temp ^ (temp >>> 32));
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "%.2f%%", value * 100);
	}

}
