package org.dungeon.io;

import java.awt.Color;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dungeon.entity.Visibility;
import org.dungeon.util.Percentage;

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
		}
	}
	
	private void addToMap(String key, String value) {
		map.put(key, toArray(value));
	}
	
	public static String[] toArray(String data) {
		if (data.startsWith("[") && data.endsWith("]")) {
			ArrayList<String> elements = new ArrayList<String>();
			int startOfLastElement = 1;
			int levelsDeep = 1;
			for (int i = 1; i < data.length(); i++) {
				char c = data.charAt(i);
				if (c == '[') {
					levelsDeep++;
				} else if (c == ']') {
					levelsDeep--;
				} else if (c == '|' && levelsDeep == 1) {
					elements.add(data.substring(startOfLastElement, i).trim());
					startOfLastElement = i + 1;
				}
			}
			elements.add(data.substring(startOfLastElement, data.length() - 1).trim());
			return elements.toArray(new String[elements.size()]);
		} else {
			return new String[] {data};
		}
	}
	
	private void readNextEntry() {
		String string = resourceParser.readString();
		if (string ==  null) {
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
	
	public Visibility readVisibility() {
		assertExists("VISIBILITY");
		if (Percentage.isValidPercentageString(getValue("VISIBILITY"))) {
			return new Visibility(Percentage.fromString(getValue("VISIBILITY")));
		} else {
			throw new IllegalStateException("this resource reader does not have a VISIBILITY value.");
		}
	}
	
	public Color readColor() {
		assertExists("COLOR");
		String[] RGB = getArrayOfValues("COLOR");
		return new Color(Integer.parseInt(RGB[0]), Integer.parseInt(RGB[1]), Integer.parseInt(RGB[2]));
	}
	
	private void logRepeatedValue() {
		String message = " repeats a value of its element (first occurence was used)!";
		DLogger.warning(filename, resourceParser.getLineNumber(), message);
	}
	
	private void logMissingColon() {
		DLogger.warning(filename, resourceParser.getLineNumber(), " does not have a colon!");
	}
	
	private void logMissingValue() {
		DLogger.warning(filename, resourceParser.getLineNumber(), " does not have a value!");
	}
	
	public char readCharacter(String key) {
		assertExists(key);
		if (getValue(key).length() == 1) {
			return getValue(key).charAt(0);
		} else {
			throw new IllegalStateException(key + " is not mapped to a single character.");
		}
	}
	
	private void assertExists(String key) {
		if (!hasValue(key)) {
			throw new IllegalStateException("current element does not have a " + key + " value.");
		}
	}

	@Override
	public void close() {
		resourceParser.close();
	}
	
	@Override
	public String toString() {
		return "ResourceReader reading " + filename + ".";
	}

}
