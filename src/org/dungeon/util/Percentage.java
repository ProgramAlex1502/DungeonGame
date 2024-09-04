package org.dungeon.util;

import java.io.Serializable;

import org.dungeon.io.DLogger;

public class Percentage implements Comparable<Percentage>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final double ONE = 1.0;
	private static final double ZERO = 0.0;
	
	private final double value;
	
	public Percentage(double percentage) {
		if (percentage < ZERO) {
			value = ZERO;
			DLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ZERO + " instead.");
		} else if (percentage > ONE) {
			value = ONE;
			DLogger.warning("Tried to use " + percentage + " as a percentage. Used " + ONE + " instead.");
		} else {
			value = percentage;
		}
	}
	
	public double toDouble() {
		return value;
	}
	
	public Percentage multiply(Percentage o) {
		return new Percentage(toDouble() * o.toDouble());
	}
	
	@Override
	public int compareTo(Percentage o) {
		return Math.fuzzyCompare(toDouble(), o.toDouble());
	}
	
	public boolean biggerThanOrEqualTo(Percentage o) {
		return compareTo(o) >= 0;
	}
	
	@Override
	public String toString() {
		return String.format("%.2f%%", value * 100);
	}

}
