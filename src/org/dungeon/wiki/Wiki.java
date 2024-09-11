package org.dungeon.wiki;

import java.util.ArrayList;
import java.util.List;

import org.dungeon.commands.IssuedCommand;
import org.dungeon.io.IO;
import org.dungeon.io.JsonObjectFactory;
import org.dungeon.util.Matches;
import org.dungeon.util.Utils;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


public abstract class Wiki {
	
	private static List<Article> articleList;
	
	private static void initialize() {
		articleList = new ArrayList<Article>();
		JsonObject jsonObject = JsonObjectFactory.makeJsonObject("wiki.json");
		for (JsonValue article : jsonObject.get("articles").asArray()) {
			JsonObject articleObject = article.asObject();
			articleList.add(new Article(articleObject.get("title").asString(), articleObject.get("content").asString()));
		}
	}
	
	public static void search(IssuedCommand issuedCommand) {
		if (articleList == null) {
			initialize();
		}
		if (issuedCommand.hasArguments()) {
			Matches<Article> matches = Utils.findBestMatches(articleList, issuedCommand.getArguments());
			if (matches.size() == 0) {
				IO.writeString("No matches were found.");
			} else if (matches.size() == 1){
				IO.writeString(matches.getMatch(0).toString());
			} else {
				StringBuilder builder = new StringBuilder();
				builder.append("The following articles match your query:\n");
				for (int i = 0; i < matches.size(); i++) {
					builder.append(toArticleListingEntry(matches.getMatch(i))).append("\n");
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
		return "  " + article.getName();
	}

}
