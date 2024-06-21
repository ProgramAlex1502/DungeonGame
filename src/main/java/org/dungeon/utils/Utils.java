package main.java.org.dungeon.utils;

import java.awt.Color;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.GameData;
import main.java.org.dungeon.game.Selectable;
import main.java.org.dungeon.help.Help;
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
	
	public static void printInvalidCommandMessage(String command) {
		IO.writeString(String.format(Constants.INVALID_COMMAND, command), Color.RED);
		if (Help.isInitialized()) {
			IO.writeString(Constants.SUGGEST_COMMANDS, Color.ORANGE);
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
		if (chance < 0 || chance > 1) {
			throw new IllegalArgumentException("chance must be nonnegative and smaller than or equal to 1.");
		}
		
		return chance > Engine.RANDOM.nextDouble();
	}
	
	public static String dateDifferenceToString(DateTime start, DateTime end) {
		Period period = new Period(start, end, PeriodType.yearMonthDay());
		int years = period.getYears();
		int months = period.getMonths();
		int days = period.getDays();
		StringBuilder builder = new StringBuilder();
		
		if (years != 0) {
			if (years == 1) {
				builder.append(years).append(" year");
			} else {
				builder.append(years).append(" years");
			}
		}
		if (months != 0) {
			if (builder.length() != 0) {
				if (days == 0) {
					builder.append(" and ");
				} else {
					builder.append(",");
				}
			}
			if (months == 1) {
				builder.append(months).append(" month");
			} else {
				builder.append(months).append(" months");
			}
		}
		if (days != 0) {
			if (builder.length() != 0) {
				builder.append(" and ");
			}
			if (days == 1) {
				builder.append(days).append(" day");
			} else {
				builder.append(days).append(" days");
			}
		}
		if (builder.length() == 0) {
			builder.append(LESS_THAN_A_DAY);
		}
		
		return builder.toString();
	}
	
	public static void printMissingArgumentsMessage() {
		switch (Engine.RANDOM.nextInt(3)) {
		case 0:
			IO.writeString("Provide some arguments.", Color.BLUE);
			break;
		case 1:
			IO.writeString("Missing arguments.", Color.BLUE);
			break;
		case 2:
			IO.writeString("This command requires arguments.", Color.BLUE);
			break;
		}
	}
	
	public static boolean startsWithIgnoreCase(String a, String b) {
		return a.toLowerCase().startsWith(b.toLowerCase());
	}
	
	public static String getAfterColon(String string) {
		int colonPosition = string.indexOf(':');
		
		if (colonPosition == -1) {
			throw new IllegalArgumentException("string does not have a colon.");
		} else {
			return string.substring(colonPosition + 1);
		}
	}
	
	public static String centerString(String string, char fill) {
		return centerString(string, Constants.COLS, fill);
	}
	
	private static String centerString(String string, int width, char fill) {
		int length = string.length();
		
		if (length > width) {
			throw new IllegalArgumentException("String is bigger than the desired width.");
		} else {
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < (width - length) / 2; i++) {
				builder.append(fill);
			}
			builder.append(string);
			
			for (int i = 0; i < (width - length) / 2; i++) {
				builder.append(fill);
			}
			if (builder.length() < width) {
				builder.append(fill);
			}
			
			return builder.toString();
		}
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
	
	public static boolean isAlphabetic(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isAlphabetic(s.charAt(i))) {
				return false;
			}
		}
		
		return true;
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
	
	public static void printAmbiguousSelectionMessage() {
		if (Engine.RANDOM.nextBoolean()) {
			IO.writeString("Provided input is ambiguous");
		} else {
			IO.writeString("More than one entity with this name could be found.");
		}
	}
	
	public static void printFailedToCreateDirectoryMessage(String directory) {
		IO.writeString("Failed to create the " + directory + " directory.");
	}
	
	public static void printLicense() {
		IO.writeString(GameData.LICENSE);
	}

}
