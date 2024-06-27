package main.java.org.dungeon.io;

import java.io.Closeable;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ResourceReader implements Closeable {

	private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	private final Map<String, String[]> map = new HashMap<String, String[]>();
	private final ResourceParser resourceParser;
	private final String filename;
	private String delimiterField;
	private Entry<String, String> entry;
		
	public ResourceReader(String filename) {
		resourceParser = new ResourceParser(new InputStreamReader(classLoader.getResourceAsStream(filename)));
		this.filename = filename;
	}
	
	public boolean readNextElement() {
		if (delimiterField == null) {
			readNextEntry();
			if (entry != null) {
				delimiterField = entry.getKey();
				do {
					if (map.containsKey(entry.getKey())) {
						logRepeatedValue();
					} else {
						addToMap(entry.getKey(), entry.getValue());
					}
					readNextEntry();
				} while (entry != null && !delimiterField.equals(entry.getKey()));
				return true;
			} else {
				return false;
			}
		} else {
			if (entry != null) {
				map.clear();
				do {
					if (map.containsKey(entry.getKey()))  {
						logRepeatedValue();
					} else {
						addToMap(entry.getKey(), entry.getValue());
					}
					readNextEntry();
				} while (entry != null && !delimiterField.equals(entry.getKey()));
				return true;
			} else {
				return false;
			}
		}
	}
	
	private void addToMap(String key, String value) {
		map.put(key, toArray(value));
	}
	
	private String[] toArray(String data) {
		if (data.startsWith("[") && data.endsWith("]")) {
			data = data.substring(1, data.length() - 1).trim();
			return data.split("\\s*\\|\\s*");
		} else {
			return new String[] {data};
		}
	}
	
	private void readNextEntry() {
		String string = resourceParser.readString();
		
		if (string == null) {
			entry = null;
		} else {
			entry = makeEntryFromString(string);
		}
	}
	
	private SimpleEntry<String, String> makeEntryFromString(String string) {
		SimpleEntry<String, String> entry = null;
		int indexOfColon = string.indexOf(":");
		if (indexOfColon == -1) {
			logMissingColon();
		} else {
			String key = string.substring(0, indexOfColon).trim();
			entry = new SimpleEntry<String, String>(key, null);
			if (indexOfColon == string.length() - 1) {
				logMissingValue();
			} else {
				entry.setValue(string.substring(indexOfColon + 1).trim());
			}
		}
		return entry;
	}
	
	public boolean hasValue(String key) {
		return map.containsKey(key);
	}
	
	public String getValue(String key) {
		String[] value = map.get(key);
		if (value.length > 1) {
			DLogger.warning("Used getValue with a nontrivial array.");
		}
		return value[0];
	}
	
	public String[] getArrayOfValues(String key) {
		return map.get(key);
	}
	
	private void logRepeatedValue() {
		DLogger.warning(filename, resourceParser.getLineNumber(), " repeats a value of its element!");
	}
	
	private void logMissingColon() {
		DLogger.warning(filename, resourceParser.getLineNumber(), " does not have a colon!");
	}
	
	private void logMissingValue() {
		DLogger.warning(filename, resourceParser.getLineNumber(), " does not have a value!");
	}
	
	@Override
	public void close() {
		resourceParser.close();
	}

}
