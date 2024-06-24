package main.java.org.dungeon.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.utils.Poem;
import main.java.org.dungeon.utils.PoemBuilder;
import main.java.org.dungeon.utils.Utils;

public final class PoetryData {
	
	private ArrayList<Poem> poems;
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
		String IDENTIFIER_TITLE = "TITLE:";
		String IDENTIFIER_AUTHOR = "AUTHOR:";
		String IDENTIFIER_CONTENT = "CONTENT:";
		
		String line;
		PoemBuilder pb = new PoemBuilder();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		DBufferedReader reader = new DBufferedReader(new InputStreamReader(cl.getResourceAsStream("poems.txt")));
		
		try {
			while ((line = reader.readString()) != null) {
				if (line.startsWith(IDENTIFIER_TITLE)) {
					if (pb.isComplete()) {
						poems.add(pb.createPoem());
						pb = new PoemBuilder();
					}
					pb.setTitle(Utils.getAfterColon(line).trim());
				} else if (line.startsWith(IDENTIFIER_AUTHOR)) {
					pb.setAuthor(Utils.getAfterColon(line).trim());
				} else if (line.startsWith(IDENTIFIER_CONTENT)) {
					pb.setAuthor(Utils.getAfterColon(line).trim());
				}
			}
			reader.close();
		} catch (IOException exception) {
			DLogger.warning(exception.toString());
		}
		
		if (pb.isComplete()) {
			poems.add(pb.createPoem());
		}
		
		DLogger.info("Loaded " + poems.size() + " poems");
		poems.trimToSize();
	}

}
