package main.java.org.dungeon.utils;

import org.joda.time.DateTime;

public abstract class Holiday {
	
	private static final String NEW_YEAR = "New Year";
	private static final String CHRISTMAS = "Christmas";
	private static final String SAINT_PATRICK = "Saint Patrick's Day";
	private static final String INDEPENDENCE_DAY = "Independence Day";
	private static final String VETERANS_DAY = "Veterans Day";
	private static final String HALLOWEEN = "Halloween";
	
	public static String getHoliday(DateTime date) {
		int month = date.getMonthOfYear();
		int day = date.getDayOfMonth();
		
		switch(month) {
		case 1:
			switch(day) {
			case 1:
				return NEW_YEAR;
			
			}
			break;
		case 3:
			switch(day) {
			case 17:
				return SAINT_PATRICK;
			}
			break;
		case 7:
			switch(day) {
			case 4:
				return INDEPENDENCE_DAY;
			}
			break;
		case 10:
			switch(day) {
			case 31:
				return HALLOWEEN;
			}
			break;
		case 11:
			switch(day) {
			case 11:
				return VETERANS_DAY;
			}
			break;
		case 12:
			switch(day) {
			case 25:
				return CHRISTMAS;
			}
			break;
		}
		return null;
	}
	
}
