package org.dungeon.date;

import java.io.Serializable;

import org.dungeon.util.DungeonMath;

public class Period implements Serializable {
	private static final long serialVersionUID = 1L;

	private final long difference;
	
	public Period(Date start, Date end) {
		difference = end.getTime() - start.getTime();
	}
	
	public Period(long start, long end) {
		difference = end - start;
	}
	
	public long getSeconds() {
		return difference / 1000;
	}
	
	@Override
	public String toString() {
		if (difference < DungeonTimeUnit.DAY.milliseconds) {
			return "Less than a day";
		}
		
		TimeStringBuilder builder = new TimeStringBuilder();
		int years = DungeonMath.safeCastLongToInteger(difference / DungeonTimeUnit.YEAR.milliseconds);
		long monthsLong = (difference % DungeonTimeUnit.YEAR.milliseconds) / DungeonTimeUnit.MONTH.milliseconds;
		int months = DungeonMath.safeCastLongToInteger(monthsLong);
		long daysLong = (difference % DungeonTimeUnit.MONTH.milliseconds) / DungeonTimeUnit.DAY.milliseconds;
		int days = DungeonMath.safeCastLongToInteger(daysLong);
		builder.set(EarthTimeUnit.YEAR, years);
		builder.set(EarthTimeUnit.MONTH, months);
		builder.set(EarthTimeUnit.DAY, days);
		return builder.toString();
	}

}
