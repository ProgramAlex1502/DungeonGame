package org.dungeon.util;

import java.math.BigInteger;

import org.dungeon.commands.IssuedCommand;
import org.dungeon.io.IO;

public class DungeonMath {
	
	private static final int SECOND_IN_NANOSECONDS = 1000000000;
	private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-8;
	private static final String TIMEOUT = "TIMEOUT";
	
	private DungeonMath() {
		throw new AssertionError();
	}
	
	public static double weightedAverage(double a, double b, Percentage bContribution) {
		return a + (b - a) * bContribution.toDouble();
	}
	
	public static double mean(double... values) {
		double sum = 0;
		for (double value : values) {
			sum += value;
		}
		return sum / values.length;
	}
	
	public static int fuzzyCompare(double a, double b) {
		return fuzzyCompare(a, b, DEFAULT_DOUBLE_TOLERANCE);
	}
	
	public static int fuzzyCompare(double a, double b, double epsilon) {
		if (a + epsilon < b) {
			return -1;
		} else if (a - epsilon > b) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public static void parseFibonacci(IssuedCommand issuedCommand) {
		int n;
		if (issuedCommand.hasArguments()) {
			for (String argument : issuedCommand.getArguments()) {
				try {
					n = Integer.parseInt(argument);
				} catch (NumberFormatException warn) {
					Messenger.printInvalidNumberFormarOrValue();
					return;
				}
				if (n < 1) {
					Messenger.printInvalidNumberFormarOrValue();
					return;
				}
				String result = fibonacci(n);
				if (result.equals(TIMEOUT)) {
					IO.writeString("Calculation exceeded the time limit.");
				} else {
					IO.writeString(functionEvaluationString("fibonacci", String.valueOf(n), fibonacci(n)));
				}
			}
		} else {
			Messenger.printMissingArgumentsMessage();
		}
	}
	
	private static String fibonacci(int n) {
		final long interruptTime = System.nanoTime() + SECOND_IN_NANOSECONDS;
		
		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		
		BigInteger s;
		for (int i = 1; i < n; i++) {
			s = a;
			a = b;
			b = b.add(s);
			if (System.nanoTime() >= interruptTime) {
				return TIMEOUT;
			}
		}
		
		return a.toString();
	}
	
	private static String functionEvaluationString(String functionName, String argument, String result) {
		String original = String.format("%s(%s) = %s", functionName, argument, result);
		return insertBreaksAtTheColumnLimit(original);
	}
	
	private static String insertBreaksAtTheColumnLimit(String string) {
		if (string.length() <= Constants.COLS) {
			return string;
		}
		StringBuilder builder = new StringBuilder();
		int charactersOnThisLine = 0;
		for (char character : string.toCharArray()) {
			if (charactersOnThisLine == Constants.COLS) {
				builder.insert(builder.length() - 1, "\\\n");
				charactersOnThisLine = 1;
			}
			builder.append(character);
			charactersOnThisLine++;
		}
		return builder.toString();
	}
	
	public static int safeCastLongToInteger(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(l + " does not fit into an integer.");
		} else {
			return (int) l;
		}
	}
	
	public static int sum(int[] integers) {
		int total = 0;
		for (int integer : integers) {
			total += integer;
		}
		return total;
	}
	
	public static void distribute(int value, int[] buckets) {
		if (buckets.length == 0) {
			throw new IllegalArgumentException("buckets must have at least one element.");
		}
		int commonModification = value / buckets.length;
		value %= buckets.length;
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] += commonModification;
			if (value > 0) {
				buckets[i]++;
				value--;
			} else if (value < 0) {
				buckets[i]--;
				value++;
			}
		}
	}

}
