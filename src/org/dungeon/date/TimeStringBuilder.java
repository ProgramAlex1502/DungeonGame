package org.dungeon.date;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dungeon.util.Utils;

public class TimeStringBuilder {
	
	private final Map<EarthTimeUnit, NonNegativeInteger> map;
	
	public TimeStringBuilder() {
		map = new EnumMap<EarthTimeUnit, NonNegativeInteger>(EarthTimeUnit.class);
	}
	
	public void set(EarthTimeUnit unit, Integer value) {
		map.put(unit, new NonNegativeInteger(value));
	}
	
	public String toString(final int fields) {
		List<String> strings = new ArrayList<String>();
		for (Entry<EarthTimeUnit, NonNegativeInteger> entry : map.entrySet()) {
			if (strings.size() < fields) {
				int value = entry.getValue().toInteger();
				if (value > 0) {
					String valueString = value + " " + entry.getKey().toString().toLowerCase();
					if (value > 1) {
						valueString += "s";
					}
					strings.add(valueString);
				}
			}
		}
		if (strings.isEmpty()) {
			return "Less than a second";
		} else {
			return Utils.enumerate(strings);
		}
	}
	
	@Override
	public String toString() {
		return toString(EarthTimeUnit.values().length);
	}

}
