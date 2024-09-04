package org.dungeon.game;

import java.util.ArrayList;

import org.dungeon.io.DLogger;
import org.dungeon.io.ResourceReader;
import org.dungeon.util.AutomaticShuffledRange;
import org.dungeon.util.Poem;
import org.dungeon.util.PoemBuilder;

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
		ResourceReader reader = new ResourceReader("poems.txt");
		String IDENTIFIER_TITLE = "TITLE";
		String IDENTIFIER_AUTHOR = "AUTHOR";
		String IDENTIFIER_CONTENT = "CONTENT";
		
		while (reader.readNextElement()) {
			PoemBuilder pb = new PoemBuilder();
			pb.setTitle(reader.getValue(IDENTIFIER_TITLE));

			pb.setAuthor(reader.getValue(IDENTIFIER_AUTHOR));
			pb.setContent(reader.getValue(IDENTIFIER_CONTENT));
			if (pb.isComplete()) {
				poems.add(pb.createPoem());
			} else {
				DLogger.warning("Parsed incomplete poem (" + reader.getValue(IDENTIFIER_TITLE) + ")!");
			}
		}
		
		reader.close();
		poems.trimToSize();
		automaticShuffledRange = new AutomaticShuffledRange(0, poems.size());
		DLogger.info("Loaded " + poems.size() + " poems.");
	}

}
