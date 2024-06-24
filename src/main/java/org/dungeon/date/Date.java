package main.java.org.dungeon.date;

import java.io.Serializable;

import main.java.org.dungeon.io.DLogger;

public class Date implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final long time;
	private static final long DAYS_IN_MONTH = 10;
	private static final long MONTHS_IN_YEAR = 10;
	private static final long millisInDay = 1000 * 60 * 60 * 24;
	private static final long millisInMonth = millisInDay * DAYS_IN_MONTH;
	private static final long millisInYear = millisInMonth * MONTHS_IN_YEAR;
	
	@SuppressWarnings("unused")
	private Date() {
		time = 0;
	}
	
	private Date(long millis) {
		time = millis;
	}
	
	public Date(long year, long month, long day) {
		if (year < 0) {
			DLogger.warning("Tried to construct Date with negative year!");
			year = 0;
		}
		if (month < 0) {
			DLogger.warning("Tried to construct Date with negative month!");
			month = 0;
		} else if (month > MONTHS_IN_YEAR) {
			DLogger.warning("Tried to construct Date with nonexistent month.");
			month = MONTHS_IN_YEAR;
		}
		if (day < 0) {
			DLogger.warning("Tried to construct Date with negative day!");
			day = 0;
		} else if (day > DAYS_IN_MONTH) {
			DLogger.warning("Tried to to construct Date with nonexistent day.");
			day = DAYS_IN_MONTH;
		}
		time = millisInYear * year + millisInMonth * month + millisInDay * day;
	}
	
	public long getDay() {
		return (time % millisInMonth) / millisInDay;
	}
	
	public long getMonth() {
		return (time % millisInYear) / millisInMonth;
	}
	
	public long getYear() {
		return time / millisInYear;
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
