package main.java.org.dungeon.utils;

import java.util.List;

import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.Selectable;
import main.java.org.dungeon.io.IO;

public class Utils {
	
	//TODO: finish Utils class
	
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
	
	public static void printAmbiguousSelectionMessage() {
		if (Engine.RANDOM.nextBoolean()) {
			IO.writeString("Provided input is ambiguous");
		} else {
			IO.writeString("More than one entity with this name could be found.");
		}
	}

}
