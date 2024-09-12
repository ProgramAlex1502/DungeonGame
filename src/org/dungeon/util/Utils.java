package org.dungeon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dungeon.game.GameData;
import org.dungeon.game.Selectable;
import org.dungeon.io.IO;

public final class Utils {
	
	private Utils() {
		throw new AssertionError();
	}
	
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
	
	public static boolean startsWithIgnoreCase(String a, String b) {
		return a.toLowerCase().startsWith(b.toLowerCase());
	}
	
	public static String[] split(String string) {
		return string.split("\\s+");
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
	
	public static String enumerate(final List<?> list) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			stringBuilder.append(list.get(i).toString());
			if (i < list.size() - 1) {
				stringBuilder.append(", ");
			} else if (i == list.size() - 2) {
				if (list.size() >= 3) {
					stringBuilder.append(",");
				}
				stringBuilder.append(" and ");
			}
		}
		return stringBuilder.toString();
	}
	
	public static <T extends Selectable> Matches<T> findBestMatches(Collection<T> collection, String... tokens) {
		return findMatches(collection, false, tokens);
	}
	
	public static <T extends Selectable> Matches<T> findBestCompleteMatches(Collection<T> collection, String... tokens) {
		return findMatches(collection, true, tokens);
	}
	
	public static <T extends Selectable> Matches<T> findMatches(Collection<T> collection, boolean complete, String... tokens) {
		List<T> listOfMatches = new ArrayList<T>();
		double maximumSimilarity = 1e-6;
		for (T candidate : collection) {
			String[] titleWords = split(candidate.getName().getSingular());
			int matches = countMatches(tokens, titleWords);
			if (!complete || matches == tokens.length) {
				double matchesOverTitleWords = matches / (double) titleWords.length;
				double matchesOverSearchArgs = matches / (double) tokens.length;
				double similarity = Math.mean(matchesOverTitleWords, matchesOverSearchArgs);
				int comparisonResult = Math.fuzzyCompare(similarity, maximumSimilarity);
				if (comparisonResult > 0) {
					maximumSimilarity = similarity;
					listOfMatches.clear();
					listOfMatches.add(candidate);
				} else if (comparisonResult == 0) {
					listOfMatches.add(candidate);
				}
			}
		}
		return Matches.fromCollection(listOfMatches);
	}
	
	private static int countMatches(String[] query, String[] entry) {
		int matches = 0;
		int indexOfLastMatchPlusOne = 0;
		for (int i = 0; i < query.length && indexOfLastMatchPlusOne < entry.length; i++) {
			for (int j = indexOfLastMatchPlusOne; j < entry.length; j++) {
				if (startsWithIgnoreCase(entry[j], query[i])) {
					indexOfLastMatchPlusOne = j + 1;
					matches++;
				}
			}
		}
		return matches;
	}

}
