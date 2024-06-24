package main.java.org.dungeon.date;

import java.io.Serializable;

import main.java.org.dungeon.io.DLogger;

public class Date implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final long time;
	private static final long daysInMonth = 10;
	private static final long monthsInYear = 10;
	private static final long millisInDay = 1000 * 60 * 60 * 24;
	private static final long millisInMonth = millisInDay * daysInMonth;
	private static final long millisInYear = millisInMonth * monthsInYear;
	
	@SuppressWarnings("unused")
	private Date() {
		time = 0;
	}
	
	private Date(long millis) {
		time = millis;
	}
	
	public Date(int year, int month, int day) {
		if (year < 0) {
			DLogger.warning("Tried to construct Date with negative year!");
			year = 0;
		}
		if (month < 0) {
			DLogger.warning("Tried to construct Date with negative month!");
			month = 0;
		}
		if (day < 0) {
			DLogger.warning("Tried to construct Date with negative day!");
			day = 0;
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
