package main.java.org.dungeon.date;

public class Period {
	
	private static final String LESS_THAN_A_DAY = "Less than a day";
	
	private final Date difference;
	
	public Period(Date start, Date end) {
		difference = new Date(end.getTime() - start.getTime());
	}
	
	public long getSeconds() {
		return difference.getTime() / 1000;
	}
	
	public String toString() {
		long years = difference.getYear();
		long months = difference.getMonth();
		long days = difference.getDay();
		StringBuilder builder = new StringBuilder();
		if (years != 0) {
			if (years == 1) {
				builder.append(years).append(" year");
			} else {
				builder.append(years).append(" years");
			}
		}
		if (months != 0) {
			if (builder.length() != 0) {
				if (days == 0) {
					builder.append(" and ");					
				} else {
					builder.append(", ");
				}
			}
			if (months == 1) {
				builder.append(months).append(" month");
			} else {
				builder.append(months).append(" months");
			}
		}
		if (days != 0) {
			if (builder.length() != 0) {
				builder.append(" and ");
			}
			if (days == 1) {
				builder.append(days).append(" day");
			} else {
				builder.append(days).append(" days");
			}
		}
		if (builder.length() == 0) {
			builder.append(LESS_THAN_A_DAY);
		}
		return builder.toString();
	}

}
