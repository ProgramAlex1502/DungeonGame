package org.dungeon.util;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.dungeon.io.DLogger;

public class StopWatch {
	
	private static final Map<TimeUnit, String> ABBREVIATIONS = new EnumMap<TimeUnit, String>(TimeUnit.class);

	static {
		ABBREVIATIONS.put(TimeUnit.NANOSECONDS, "ns");
		ABBREVIATIONS.put(TimeUnit.MICROSECONDS, "µs");
		ABBREVIATIONS.put(TimeUnit.MILLISECONDS, "ms");
		ABBREVIATIONS.put(TimeUnit.SECONDS, "s");
	}
	
	private final long time;
	
	public StopWatch() {
		time = System.nanoTime();
	}
	
	private long calculateTimeDifference() {
		return System.nanoTime() - time;
	}
	
	public String toString(TimeUnit unit) {
		long timeDifference = calculateTimeDifference();
		if (ABBREVIATIONS.containsKey(unit)) {
			return unit.convert(timeDifference, TimeUnit.NANOSECONDS) + " " + ABBREVIATIONS.get(unit);
		} else {
			DLogger.warning("Passed a TimeUnit that does not have a defined abbreviation to StopWatch.toString(TimeUnit)!");
			return null;
		}
	}
	
	@Override
	public String toString() {
		return toString(TimeUnit.MILLISECONDS);
	}

}
