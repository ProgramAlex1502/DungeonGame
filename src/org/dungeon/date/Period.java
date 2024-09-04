package org.dungeon.date;

import java.io.Serializable;

public class Period implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String LESS_THAN_A_DAY = "Less than a day";
	
	private final long difference;
	
	public Period(Date start, Date end) {
		difference = end.getTime() - start.getTime();
	}
	
	public long getSeconds() {
		return difference / 1000;
	}
	
	@Override
	public String toString() {
		long years = difference / Date.MILLIS_IN_YEAR;
		long months = (difference % Date.MILLIS_IN_YEAR) / Date.MILLIS_IN_MONTH;
		long days = (difference % Date.MILLIS_IN_MONTH) / Date.MILLIS_IN_DAY;
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
