package main.java.org.dungeon.util;

public class Percentage {
	
	private static final double ONE = 1.0;
	private static final double ZERO = 0.0;
	private double value;
	
	public Percentage(double percentage) {
		if (percentage < ZERO) {
			value = ZERO;
		} else if (percentage > ONE) {
			value = ONE;
		} else {
			value = percentage;
		}
	}
	
	public double toDouble() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("%.2f%%", value * 100);
	}

}
