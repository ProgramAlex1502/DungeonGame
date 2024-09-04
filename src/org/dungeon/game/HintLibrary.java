package org.dungeon.game;

import java.util.ArrayList;

import org.dungeon.io.DLogger;
import org.dungeon.io.ResourceReader;
import org.dungeon.util.AutomaticShuffledRange;

public class HintLibrary extends Library {
	
	private final ArrayList<String> hints = new ArrayList<String>();
	
	private AutomaticShuffledRange automaticShuffledRange;
	
	public int getHintCount() {
		if (isUninitialized()) {
			initialize();
		}
		return hints.size();
	}
	
	public String getNextHint() {
		return hints.get(automaticShuffledRange.getNext());
	}

	@Override
	void load() {
		ResourceReader reader = new ResourceReader("hints.txt");
		final String HINT = "HINT";
		while(reader.readNextElement()) {
			hints.add(reader.getValue(HINT));
		}
		reader.close();
		hints.trimToSize();
		automaticShuffledRange = new AutomaticShuffledRange(0, hints.size());
		DLogger.info("Loaded " + hints.size() + " hints.");
	}

}
