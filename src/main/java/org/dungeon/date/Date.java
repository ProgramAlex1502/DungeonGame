package main.java.org.dungeon.date;

import java.io.Serializable;

import main.java.org.dungeon.io.DLogger;

public class Date implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final long SECONDS_IN_MINUTE = 60;
	public static final long MINUTES_IN_HOUR = 60;
	public static final long HOURS_IN_DAY = 24;
	public static final long DAYS_IN_MONTH = 10;
	public static final long MONTHS_IN_YEAR = 10;
	
	private static final long millisInSecond = 1000;
	private static final long millisInMinute = millisInSecond * SECONDS_IN_MINUTE;
	private static final long millisInHour = millisInMinute * MINUTES_IN_HOUR;
	private static final long millisInDay = millisInHour * HOURS_IN_DAY;
	private static final long millisInMonth = millisInDay * DAYS_IN_MONTH;
	private static final long millisInYear = millisInMonth * MONTHS_IN_YEAR;

	private long time;
	
	public Date(long millis) {
		time = millis;
	}
	
	public Date(long year, long month, long day, long hour, long minute, long second) {
		this(year, month, day);
		if (hour < 0) {
			DLogger.warning("Tried to construct Date with negative hour!");
			hour = 0;
		} else if (hour >= HOURS_IN_DAY) {
			DLogger.warning("Tried to construct Date with nonexistent hour.");
			hour = HOURS_IN_DAY;
		}
		if (minute < 0) {
		    DLogger.warning("Tried to construct Date with negative minute!");
		    minute = 0;
		} else if (minute >= MINUTES_IN_HOUR) {
		    DLogger.warning("Tried to construct Date with nonexistent minute.");
		    minute = MINUTES_IN_HOUR;
		}
		if (second < 0) {
		    DLogger.warning("Tried to construct Date with negative second!");
		    second = 0;
		} else if (second >= SECONDS_IN_MINUTE) {
		    DLogger.warning("Tried to construct Date with nonexistent second.");
		    second = SECONDS_IN_MINUTE;
		}
		time += hour * millisInHour + minute * millisInMinute + second * millisInSecond;
	}
	
	public Date(long year, long month, long day) {
		if (year <= 0) {
			DLogger.warning("Tried to construct Date with nonpositive year!");
			year = 1;
		}
		if (month <= 0) {
			DLogger.warning("Tried to construct Date with nonpositive month!");
			month = 1;
		} else if (month > MONTHS_IN_YEAR) {
			DLogger.warning("Tried to construct Date with nonexistent month.");
			month = MONTHS_IN_YEAR;
		}
		if (day <= 0) {
			DLogger.warning("Tried to construct Date with nonpositive day!");
			day = 1;
		} else if (day > DAYS_IN_MONTH) {
			DLogger.warning("Tried to to construct Date with nonexistent day.");
			day = DAYS_IN_MONTH;
		}
		time = millisInYear * (year - 1) + millisInMonth * (month - 1) + millisInDay * (day - 1);
	}
	
	public long getTime() {
		return time;
	}
	
	public long getDay() {
		return (time % millisInMonth) / millisInDay + 1;
	}
	
	public long getMonth() {
		return (time % millisInYear) / millisInMonth + 1;
	}
	
	public long getYear() {
		return time / millisInYear + 1;
	}
	
	public Date minusDays(int days) {
		if (days < 0) {
			DLogger.warning("Passed negative argument to minus method.");
		}
		return new Date(time - days * millisInDay);
	}
	
	public Date minusMonths(int months) {
	    if (months < 0) {
	      DLogger.warning("Passed negative argument to minus method.");
	    }
	    return new Date(time - months * millisInMonth);
	  }

	  public Date minusYears(int years) {
	    if (years < 0) {
	      DLogger.warning("Passed negative argument to minus method.");
	    }
	    return new Date(time - years * millisInYear);
	  }

	  public Date plusDays(int days) {
	    if (days < 0) {
	      DLogger.warning("Passed negative argument to plus method.");
	    }
	    return new Date(time + days * millisInDay);
	  }

	  public Date plusMonths(int months) {
	    if (months < 0) {
	      DLogger.warning("Passed negative argument to plus method.");
	    }
	    return new Date(time + months * millisInMonth);
	  }

	  public Date plusYears(int years) {
	    if (years < 0) {
	      DLogger.warning("Passed negative argument to plus method.");
	    }
	    return new Date(time + years * millisInYear);
	  }

}
