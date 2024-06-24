package main.java.org.dungeon.game;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

class ResourceReader implements Closeable {

	private final HashMap<String, String> map;
	private final DBufferedReader dBufferedReader;
	
	public ResourceReader(InputStream inputStream) {
		map = new HashMap<String, String>();
		dBufferedReader = new DBufferedReader(new InputStreamReader(inputStream));
	}
	
	public boolean contains(String key) {
		return map.containsKey(key);
	}
	
	public String getValue(String identifier) {
		return map.get(identifier);
	}
	
	public boolean readNextElement() {
		map.clear();
		boolean readNewValue = true;
		while (readNewValue) {
			readNewValue = readNextValue();
		}
		
		return !map.isEmpty();
	}
	
	private boolean readNextValue() {
		String string = dBufferedReader.readString();
		if (string != null && !string.isEmpty()) {
			int colonIndex = string.indexOf(':');
			String valueId = string.substring(0, colonIndex).trim();
			if (!map.containsKey(valueId)) {
				map.put(valueId, string.substring(colonIndex + 1).trim());
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void close() throws IOException {
		dBufferedReader.close();
	}

}
