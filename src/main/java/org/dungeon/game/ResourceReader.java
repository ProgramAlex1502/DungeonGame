package main.java.org.dungeon.game;

import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import main.java.org.dungeon.io.DLogger;

class ResourceReader implements Closeable {

	private final HashMap<String, String> map;
	private final ResourceParser resourceParser;
	
	private Pair<String, String> lastPair;
	
	public ResourceReader(InputStream inputStream) {
		map = new HashMap<String, String>();
		resourceParser = new ResourceParser(new InputStreamReader(inputStream));
	}
	
	private Pair<String, String> makePairFromString(String string) {
		String[] parts = {"", ""};
		
		int indexOfColon = string.indexOf(":");
		if (indexOfColon == -1) {
			DLogger.warning("Resource String without color!");
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
	
	public boolean contains(String key) {
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
