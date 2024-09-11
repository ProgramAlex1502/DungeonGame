package org.dungeon.date;

public enum DungeonTimeUnit {
	
	MILLISECOND(1), SECOND(1000 * MILLISECOND.milliseconds), MINUTE(60 * SECOND.milliseconds),
	HOUR(60 * MINUTE.milliseconds), DAY(24 * HOUR.milliseconds), MONTH(10 * DAY.milliseconds),
	YEAR(10 * MONTH.milliseconds);
	
	public final long milliseconds;
	
	DungeonTimeUnit(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	
	public long as(DungeonTimeUnit unit) {
		if (this.milliseconds <= unit.milliseconds) {
			throw new IllegalArgumentException("unit is bigger than or equal to the caller");
		} else {
			return this.milliseconds / unit.milliseconds;
		}
	}

}
