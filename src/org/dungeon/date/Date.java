package org.dungeon.date;

import java.io.Serializable;

import org.dungeon.io.DLogger;

import static org.dungeon.date.DungeonTimeUnit.DAY;
import static org.dungeon.date.DungeonTimeUnit.HOUR;
import static org.dungeon.date.DungeonTimeUnit.MINUTE;
import static org.dungeon.date.DungeonTimeUnit.MONTH;
import static org.dungeon.date.DungeonTimeUnit.SECOND;
import static org.dungeon.date.DungeonTimeUnit.YEAR;

public class Date implements Comparable<Date>, Serializable {
	private static final long serialVersionUID = 1L;
	
	private long time;
	
	private Date(long millis) {
		time = millis;
	}
	
	public Date(long year, long month, long day, long hour, long minute, long second) {
		this(year, month, day);
		if (hour < 0) {
			DLogger.warning("Tried to construct Date with negative hour!");
			hour = 0;
		} else if (hour >= DAY.as(HOUR)) {
			DLogger.warning("Tried to construct Date with nonexistent hour.");
			hour = DAY.as(HOUR);
		}
		if (minute < 0) {
			DLogger.warning("Tried to construct Date with negative minute!");
			minute = 0;
		} else if (minute >= HOUR.as(MINUTE)) {
			DLogger.warning("Tried to construct Date with nonexistent minute.");
			minute = HOUR.as(MINUTE);
		}
		if (second < 0) {
			DLogger.warning("Tried to construct Date with negative second!");
			second = 0;
		} else if (second >= MINUTE.as(SECOND)) {
			DLogger.warning("Tried to construct Date with nonexistent second.");
			second = MINUTE.as(SECOND);
		}
		time += hour * HOUR.milliseconds + minute * MINUTE.milliseconds + second * SECOND.milliseconds;
	}
	
	public Date(long year, long month, long day) {
		if (year <= 0) {
			DLogger.warning("Tried to construct Date with nonpositive year!");
			year = 1;
		}
		if (month <= 0) {
			DLogger.warning("Tried to construct Date with nonpositive month!");
			month = 1;
		} else if (month > YEAR.as(MONTH)) {
			DLogger.warning("Tried to construct Date with nonexistent month.");
			month = YEAR.as(MONTH);
		}
		if (day <= 0) {
			DLogger.warning("Tried to construct Date with nonpositive day!");
			day = 1;
		} else if (day > MONTH.as(DAY)) {
			DLogger.warning("Tried to construct Date with nonexistent day.");
			day = MONTH.as(DAY);
		}
		time = YEAR.milliseconds * (year - 1) + MONTH.milliseconds * (month - 1) + DAY.milliseconds * (day - 1);
	}
	
	public long getTime() {
		return time;
	}
	
	private long getSecond() {
		return (time % MINUTE.milliseconds) / SECOND.milliseconds;
	}
	
	private long getMinute() {
		return (time % HOUR.milliseconds) / MINUTE.milliseconds;
	}
	
	public long getHour() {
		return (time % DAY.milliseconds) / HOUR.milliseconds;
	}
	
	public long getDay() {
		return (time % MONTH.milliseconds) / DAY.milliseconds + 1;
	}
	
	public long getMonth() {
		return (time % YEAR.milliseconds) / MONTH.milliseconds + 1;
	}
	
	public long getYear() {
		return time / YEAR.milliseconds + 1;
	}
	
	public Date plus(int amount, DungeonTimeUnit unit) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be positive.");
		} else if (unit == null) {
			throw new IllegalArgumentException("unit should not be null.");
		} else {
			return new Date(time + amount * unit.milliseconds);
		}
	}
	
	public Date minus(int amount, DungeonTimeUnit unit) {
		if (amount <= 0) {
			throw new IllegalArgumentException("amount must be positive.");
		} else if (unit == null) {
			throw new IllegalArgumentException("unit should not be null.");
		} else {
			return new Date(time - amount * unit.milliseconds);
		}
	}
	
	public String toDateString() {
		return "day" + getDay() + " of month " + getMonth() +"of the year " + getYear();
	}
	
	public String toTimeString() {
		return String.format("%02d:%02d:%02d", getHour(), getMinute(), getSecond());
	}
	
	@Override
	public int compareTo(Date date) {
		if (time > date.time) {
			return 1;
		} else if (time == date.time) {
			return 0;
		} else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		String format= "%d-%02d-%02d %02d:%02d:%02d";
		return String.format(format, getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond());
	}
	
}
