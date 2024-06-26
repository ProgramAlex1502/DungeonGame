package main.java.org.dungeon.io;

import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import main.java.org.dungeon.game.Pair;

public class ResourceReader implements Closeable {

	private final HashMap<String, String> map = new HashMap<String, String>();
	private final ResourceParser resourceParser;
	private final String filename;
	
	private Pair<String, String> lastPair;
	
	public ResourceReader(InputStream inputStream, String filename) {
		resourceParser = new ResourceParser(new InputStreamReader(inputStream));
		this.filename = filename;
	}
	
	private Pair<String, String> makePairFromString(String string) {
		String[] parts = {"", ""};
		
		int indexOfColon = string.indexOf(":");
		if (indexOfColon == -1) {
			logResourceStringWithoutColon();
		} else {
			parts[0] = string.substring(0, indexOfColon).trim();
			if (indexOfColon == string.length() - 1) {
				DLogger.warning("Resource String with nothing after the colon!");
			} else {
				parts[1] = string.substring(indexOfColon + 1).trim();
			}
		}
		return new Pair<String, String>(parts[0], parts[1]);
	}
	
	private void logResourceStringWithoutColon() {
		String location = "Line " + resourceParser.getLineNumber() + " of " + filename;
		DLogger.warning(location + " does not have a colon!");
	}
	
	public boolean hasValue(String key) {
		return map.containsKey(key);
	}
	
	public String getValue(String identifier) {
		return map.get(identifier);
	}
	
	public boolean readNextElement() {
		map.clear();

		if (lastPair != null) {
			map.put(lastPair.a, lastPair.b);
		}
		
		while (true) {
			lastPair = readNextPair();
			if (map.containsKey("ID")) {
				if (lastPair == null || lastPair.a.equals("ID")) {
					return true;
				}
			} else {
				if (lastPair == null) {
					return false;
				}
			}
			map.put(lastPair.a, lastPair.b);
		}
	}
	
	private Pair<String, String> readNextPair() {
		String string = resourceParser.readString();

		if (string != null) {
			return makePairFromString(string);
		}
		
		return null;
	}
	
	@Override
	public void close() {
		resourceParser.close();
	}

}
