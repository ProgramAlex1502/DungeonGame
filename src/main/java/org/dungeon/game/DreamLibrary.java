package main.java.org.dungeon.game;

import java.util.ArrayList;

import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.ResourceReader;
import main.java.org.dungeon.util.AutomaticShuffledRange;

public class DreamLibrary extends Library {
	
	private final ArrayList<String> dreams = new ArrayList<String>();
	private AutomaticShuffledRange automaticShuffledRange;
	
	public String getNextDream() {
		if (!isInitialized()) {
			initialize();
		}
		return dreams.get(automaticShuffledRange.getNext());
	}

	@Override
	void load() {
		ResourceReader reader = new ResourceReader("dreams.txt");
		while (reader.readNextElement()) {
			dreams.add(reader.getValue("DREAM"));
		}
		reader.close();
		dreams.trimToSize();
		automaticShuffledRange = new AutomaticShuffledRange(0, dreams.size());
		DLogger.info("Loaded " + dreams.size() + " dreams.");
	}

}
