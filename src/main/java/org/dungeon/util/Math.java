package main.java.org.dungeon.util;

import java.awt.Color;
import java.math.BigInteger;

import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.io.IO;

public class Math {
	
	private static final int FIBONACCI_MAX = 65535;
	private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-8;
	
	public static double mean(double... values) {
		double sum = 0;
		for (double value : values) {
			sum += value;
		}
		return sum / values.length;
	}
	
	public static int fuzzyCompare(Double d1, Double d2) {
		return fuzzyCompare(d1, d2, DEFAULT_DOUBLE_TOLERANCE);
	}
	
	public static int fuzzyCompare(Double d1, Double d2, Double epsilon) {
		if (d1 + epsilon < d2) {
			return -1;
		} else if (d1 - epsilon > d2) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public static void fibonacci(IssuedCommand issuedCommand) {
		int intArgument;
		
		if (issuedCommand.hasArguments()) {
			for (String strArgument : issuedCommand.getArguments()) {
				try {
					intArgument = Integer.parseInt(strArgument);
				} catch (NumberFormatException warn) {
					Messenger.printInvalidNumberFormatOrValue();
					return;
				}
				if (0 < intArgument && intArgument <= FIBONACCI_MAX) {
					char[] result = fibonacci(intArgument).toCharArray();
					StringBuilder sb = new StringBuilder(result.length + 64);
					sb.append("fibonacci").append('(').append(intArgument).append(')').append(" = ");
					int newLineCharacters = 0;
					for (char character : result) {
						if ((sb.length() + 1 - newLineCharacters) % Constants.COLS == 0) {
							sb.append("\\\n");
							newLineCharacters++;
						} else {
							sb.append(character);
						}
					}
					IO.writeString(sb.toString());
				} else {
					IO.writeString("n must be positive and smaller than " + (FIBONACCI_MAX + 1) + ".", Color.ORANGE);
				}
			}
		} else {
			Messenger.printMissingArgumentsMessage();
		}
	}
	
	private static String fibonacci(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("n must be positive.");
		}
		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		
		BigInteger s;
		for (int i = 1; i < n; i++) {
			s = a;
			a = b;
			b = b.add(s);
		}
		return a.toString();
	}

}
