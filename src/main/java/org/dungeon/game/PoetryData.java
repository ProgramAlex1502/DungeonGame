package main.java.org.dungeon.game;

import java.util.ArrayList;

import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.util.Poem;
import main.java.org.dungeon.util.PoemBuilder;

public final class PoetryData {
	
	private final ArrayList<Poem> poems;
	private boolean initialized;
	
	public PoetryData() {
		poems = new ArrayList<Poem>();
	}
	
	public int getPoemCount() {
		if (!initialized) {
			initialize();
		}
		return poems.size();
	}
	
	public Poem getPoem(int index) {
		return poems.get(index);
	}
	
	private void initialize() {
		loadPoems();
		initialized = true;
	}
	
	private void loadPoems() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		@SuppressWarnings("resource")
		ResourceReader reader = new ResourceReader(classLoader.getResourceAsStream("poems.txt"));
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
			}
		}
		
		DLogger.info("Loaded " + poems.size() + " poems");
		poems.trimToSize();
	}

}
