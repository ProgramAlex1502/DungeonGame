package org.dungeon.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.dungeon.io.DLogger;
import org.dungeon.util.Percentage;

public class Weight implements Comparable<Weight>, Serializable {
	private static final long serialVersionUID = 1L;

	public static final Weight ZERO = newInstance(0);
	private static final DecimalFormat WEIGHT_FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.US);
	
	static {
		WEIGHT_FORMAT.applyPattern("0.### kg");
	}
	
	private final double value;
	
	private Weight(double value) {
		this.value = value;
	}
	
	public static Weight newInstance(double value) {
		if (value < 0) {
			DLogger.warning("Tried to create Weight from negative double.");
			return ZERO;
		}
		return new Weight(value);
	}
	
	public Weight add(Weight o) {
		return newInstance(this.value + o.value);
	}
	
	public Weight multiply(Percentage p) {
		return newInstance(this.value * p.toDouble());
	}
	
	@Override
	public int compareTo(Weight weight) {
		return Double.compare(value, weight.value);
	}

	@Override
	public String toString() {
		return WEIGHT_FORMAT.format(value);
	}


}
