package main.java.org.dungeon.wiki;

import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.io.ResourceReader;
import main.java.org.dungeon.util.Math;
import main.java.org.dungeon.util.Utils;

public abstract class Wiki {
	
	private static List<Article> articleList;
	
	private Wiki() {
	}
	
	private static void initialize() {
		articleList = new ArrayList<Article>();
		ResourceReader reader = new ResourceReader("wiki.txt");
		while (reader.readNextElement()) {
			articleList.add(new Article(reader.getValue("ARTICLE"), reader.getValue("CONTENT")));
		}
	}
	
	public static void search(IssuedCommand issuedCommand) {
		if (articleList == null) {
			initialize();
		}
		if (issuedCommand.hasArguments()) {
			List<Article> matches = findMatches(issuedCommand.getArguments());
			if (matches.isEmpty()) {
				IO.writeString("No matches were found.");
			} else if (matches.size() == 1) {
				IO.writeString(matches.get(0).toString());
			} else {
				StringBuilder builder = new StringBuilder();
				builder.append("The following articles match your query:\n");
				for (Article match : matches) {
					builder.append(toArticleListingEntry(match)).append("\n");
				}
				builder.append("Be more specific.");
				IO.writeString(builder.toString());
			}
		} else {
			writeArticleList();
		}
	}
	
	private static void writeArticleList() {
		StringBuilder builder = new StringBuilder();
		builder.append("The wiki has the following ").append(articleList.size()).append(" articles:\n");
		for (Article article : articleList) {
			builder.append(toArticleListingEntry(article)).append("\n");
		}
		IO.writeString(builder.toString());
	}
	
	private static String toArticleListingEntry(Article article) {
		return " " + article.title;
	}
	
	private static List<Article> findMatches(String[] searchArguments) {
		List<Article> listOfMatches = new ArrayList<Article>();
		double maximumSimilarity = 1e-6;
		for (Article article : articleList) {
			String[] titleWords = Utils.split(article.title);
			int matches = countMatches(searchArguments, titleWords);
			double matchesOverTitleWords = matches / (double) titleWords.length;
			double matchesOverSearchArgs = matches / (double) searchArguments.length;
			double similarity = Math.mean(matchesOverTitleWords, matchesOverSearchArgs);
			int comparisonResult = Math.fuzzyCompare(similarity, maximumSimilarity);
			if (comparisonResult > 0) {
				maximumSimilarity -= similarity;
				listOfMatches.clear();
				listOfMatches.add(article);
			} else if (comparisonResult == 0) {
				listOfMatches.add(article);
			}
		}
		
		return listOfMatches;
	}
	
	private static int countMatches(String[] query, String[] entry) {
		int matches = 0;
		int indexOfLastMatchPlusOne = 0;
		for (int i = 0; i < query.length && indexOfLastMatchPlusOne < entry.length; i++) {
			for (int j = indexOfLastMatchPlusOne; j < entry.length; j++) {
				if (Utils.startsWithIgnoreCase(entry[j], query[i])) {
					indexOfLastMatchPlusOne = j + 1;
					matches++;
				}
			}
		}
		
		return matches;
	}

}
