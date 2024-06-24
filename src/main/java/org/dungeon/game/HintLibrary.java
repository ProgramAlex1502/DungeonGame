package main.java.org.dungeon.game;

import java.util.ArrayList;

import main.java.org.dungeon.io.DLogger;

public class HintLibrary extends Library{
	
	private final ArrayList<String> hints = new ArrayList<String>();
	
	public int getHintCount() {
		if (!isInitialized()) {
			initialize();
		}
		
		return hints.size();
	}
	
	public String getHint(int index) {
		return hints.get(index);
	}

	@Override
	void load() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ResourceReader reader = new ResourceReader(classLoader.getResourceAsStream("hints.txt"));
		final String HINT = "HINT";
		while (reader.readNextElement()) {
			hints.add(reader.getValue(HINT));
		}
		reader.close();
		hints.trimToSize();
		DLogger.info("Loaded " + hints.size() + " hints.");
	}

}
