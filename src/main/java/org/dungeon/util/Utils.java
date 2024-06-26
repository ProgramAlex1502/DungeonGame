package main.java.org.dungeon.util;

import java.util.List;

import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.GameData;
import main.java.org.dungeon.game.Selectable;
import main.java.org.dungeon.io.IO;

public class Utils {
		
	public static String LESS_THAN_A_DAY = "Less than a day";
	
	public static String padString(String original, int desiredLength) {
		int requiredSpaces = desiredLength - original.length();
		if (requiredSpaces > 0) {
			StringBuilder stringBuilder = new StringBuilder(desiredLength);
			stringBuilder.append(original);
			for (int i = 0; i < requiredSpaces; i++) {
				stringBuilder.append(' ');
			}
			return stringBuilder.toString();
		} else {
			return original;
		}
	}
	
	public static <T extends Selectable> SelectionResult<T> selectFromList(List<T> candidates, String[] tokens) {
		SelectionResult<T> selectionResult = new SelectionResult<T>();
		
		for (T candidate : candidates) {
			if (checkQueryMatch(tokens, split(candidate.getName()))) {
				selectionResult.addMatch(candidate);
			}
		}
		
		return selectionResult;
	}
	
	public static boolean checkQueryMatch(String[] query, String[] candidate) {
		if (query.length > candidate.length) {
			return false;
		}
		
		boolean foundMatch = false;
		int indexOfLastMatchInCandidate = 0;
		
		for (String queryToken : query) {
			while (indexOfLastMatchInCandidate < candidate.length && !foundMatch) {
				if (startsWithIgnoreCase(candidate[indexOfLastMatchInCandidate], queryToken)) {
					foundMatch = true;
				}
				indexOfLastMatchInCandidate++;
			}
			
			if (!foundMatch) {
				return false;
			} else {
				foundMatch = false;
			}
		}
		
		return true;
	}
	
	public static boolean roll(double chance) {
		chance = (new Percentage(chance)).toDouble();
		
		return chance > Engine.RANDOM.nextDouble();
	}
	
	public static boolean startsWithIgnoreCase(String a, String b) {
		return a.toLowerCase().startsWith(b.toLowerCase());
	}
	
	public static String makeRepeatedCharacterString(int repetitions, char character) {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < repetitions; i++) {
			builder.append(character);
		}
		
		return builder.toString();
	}
	
	public static String[] split(String string) {
		return string.split("\\s+");
	}
	
	public static boolean isNotBlankString(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isWhitespace(c)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String clearEnd(String str) {
		StringBuilder stringBuilder = new StringBuilder(str);
		int length = stringBuilder.length();
		char lastChar;
		
		while (length > 0) {
			lastChar = stringBuilder.charAt(length - 1);
			if (Character.isSpaceChar(lastChar) || lastChar == '\n') {
				stringBuilder.setLength(stringBuilder.length() - 1);
			} else {
				break;
			}
			
			length = stringBuilder.length();
		}
		
		return stringBuilder.toString();
	}
	
	public static String join(String delimiter, String... elements) {
		if (elements.length == 0) {
			throw new IllegalArgumentException("elements must have at least one element.");
		}
		
		StringBuilder sb = new StringBuilder(elements.length * (delimiter.length() + elements[0].length()) + 16);
		sb.append(elements[0]);
		
		for (int i = 1; i < elements.length; i++) {
			sb.append(delimiter).append(elements[i]);
		}
		
		return sb.toString();
	}
	
	public static String bytesToHuman(long bytes) {
		if (bytes < 1024) {
			return bytes + " B";
		}
		
		int bitsUsed = 63 - Long.numberOfLeadingZeros(bytes);
		
		double significand = (double) bytes / (1L << (bitsUsed - bitsUsed % 10));
		
		char prefix = "kMGTPE".charAt(bitsUsed / 10 - 1);
		return String.format("%.1f %sB", significand, prefix);
	}
	
	public static void printLicense() {
		IO.writeString(GameData.LICENSE);
	}
	
	public static String stringArrayToString(String[] strings, String separator) {
		if (strings.length == 0) {
			return "";
		} else if (strings.length == 1) {
			return strings[0];
		} else {
			StringBuilder builder = new StringBuilder(strings[0]);
			for (int index = 1; index < strings.length; index++) {
				builder.append(separator);
				builder.append(strings[index]);
			}
			return builder.toString();
		}
	}

}
