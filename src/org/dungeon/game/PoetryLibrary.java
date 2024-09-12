package org.dungeon.game;

import java.util.ArrayList;

import org.dungeon.io.DLogger;
import org.dungeon.io.JsonObjectFactory;
import org.dungeon.util.AutomaticShuffledRange;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public final class PoetryLibrary extends Library {
	
	private final ArrayList<Poem> poems = new ArrayList<Poem>();
	
	private AutomaticShuffledRange automaticShuffledRange;
	
	public int getPoemCount() {
		if (isUninitialized()) {
			initialize();
		}
		return poems.size();
	}
	
	public Poem getPoem(int index) {
		return poems.get(index);
	}
	
	public Poem getNextPoem() {
		return poems.get(automaticShuffledRange.getNext());
	}
	
	@Override
	void load() {
		JsonObject jsonObject = JsonObjectFactory.makeJsonObject("poems.json");
		for (JsonValue poem : jsonObject.get("poems").asArray()) {
			JsonObject poemObject = poem.asObject();
			String title = poemObject.get("title").asString();
			String author = poemObject.get("author").asString();
			String content = poemObject.get("content").asString();
			poems.add(new Poem(title, author, content));
		}
		
		poems.trimToSize();
		automaticShuffledRange = new AutomaticShuffledRange(0, poems.size());
		DLogger.info("Loaded " + poems.size() + " poems.");
	}

}
