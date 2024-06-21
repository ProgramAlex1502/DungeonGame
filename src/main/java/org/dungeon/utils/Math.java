package main.java.org.dungeon.utils;

import java.awt.Color;
import java.math.BigInteger;

import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.io.IO;

public class Math {
	
	private static final int FIBONACCI_MAX = 10000;
	
	public static void fibonacci(IssuedCommand issuedCommand) {
		int intArgument;
		
		if (issuedCommand.hasArguments()) {
			for (String strArgument : issuedCommand.getArguments()) {
				try {
					intArgument = Integer.parseInt(strArgument);
				} catch (NumberFormatException ignore) {
					IO.writeString(String.format("Failed to parse " + strArgument + ".", Color.RED));
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
					IO.writeString("n must be positive and smaller than " + FIBONACCI_MAX + ".", Color.ORANGE);
				}
			}
		} else {
			Utils.printMissingArgumentsMessage();
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
