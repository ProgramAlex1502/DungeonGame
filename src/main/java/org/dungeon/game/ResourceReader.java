package main.java.org.dungeon.game;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

class ResourceReader implements Closeable {

	private final HashMap<String, String> map;
	private final ResourceParser dBufferedReader;
	
	private Pair<String, String> lastPair;
	
	public ResourceReader(InputStream inputStream) {
		map = new HashMap<String, String>();
		dBufferedReader = new ResourceParser(new InputStreamReader(inputStream));
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
		String string = dBufferedReader.readString();
		if (string != null && !string.isEmpty()) {
			int colonIndex = string.indexOf(':');
			String key = string.substring(0, colonIndex).trim();
			String value = string.substring(colonIndex + 1).trim();
			return new Pair<String, String>(key, value);
		}
		
		return null;
	}
	
	@Override
	public void close() throws IOException {
		dBufferedReader.close();
	}

}
