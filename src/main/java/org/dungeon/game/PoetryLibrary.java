package main.java.org.dungeon.game;

import java.util.ArrayList;

import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.util.Poem;
import main.java.org.dungeon.util.PoemBuilder;

public final class PoetryLibrary extends Library{
	
	private final ArrayList<Poem> poems = new ArrayList<Poem>();
	
	public int getPoemCount() {
		if (!isInitialized()) {
			initialize();
		}
		return poems.size();
	}
	
	public Poem getPoem(int index) {
		return poems.get(index);
	}
	
	@Override
	void load() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ResourceReader reader = new ResourceReader(classLoader.getResourceAsStream("poems.txt"));
		final String IDENTIFIER_ID = "ID";
		final String IDENTIFIER_TITLE = "TITLE";
		final String IDENTIFIER_AUTHOR = "AUTHOR";
		final String IDENTIFIER_CONTENT = "CONTENT";
		
		while (reader.readNextElement()) {
			PoemBuilder pb = new PoemBuilder();
			pb.setTitle(reader.getValue(IDENTIFIER_TITLE));
			pb.setAuthor(reader.getValue(IDENTIFIER_AUTHOR));
			pb.setContent(reader.getValue(IDENTIFIER_CONTENT));
			if (pb.isComplete()) {
				poems.add(pb.createPoem());
			} else {
				DLogger.warning("Parsed incomplete poem! (ID = " + reader.getValue(IDENTIFIER_ID) + ")");
			}
		}
		
		reader.close();
		poems.trimToSize();
		DLogger.info("Loaded " + poems.size() + " poems");
	}

}
