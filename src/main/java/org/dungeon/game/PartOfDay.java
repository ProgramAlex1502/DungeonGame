package main.java.org.dungeon.game;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public enum PartOfDay {
		
	NIGHT("Night", 0.4, 1),
	DAWN("Dawn", 0.6, 5),
	MORNING("Morning", 0.8, 7),
	NOON("Noon", 1.0, 11),
	AFTERNOON("Afternoon", 0.8, 13),
	DUSK("Dusk", 0.6, 17),
	EVENING("Evening", 0.4, 19),
	MIDNIGHT("Midnight", 0.2, 23);
	
	private final String stringRepresentation;
	
	private double luminosity;
	
	private int startingHour;
	
	PartOfDay(String stringRepresentation, double luminosity, int startingHour) {
		this.stringRepresentation = stringRepresentation;
		setLuminosity(luminosity);
		setStartingHour(startingHour);
	}
	
	public static PartOfDay getCorrespondingConstants(DateTime dateTime) {
		int hour = dateTime.getHourOfDay();
		
		if (hour == 0) {
			return MIDNIGHT;
		}
		
		PartOfDay[] podArray = values();
		
		for (int i = podArray.length - 1; i >= 0; i--) {
			if (podArray[i].getStartingHour() <= hour) {
				return podArray[i];
			}
		}
		
		return null;
	}
	
	public static int getSecondsToNext(DateTime cur, PartOfDay pod) {
		DateTime startOfPod = cur.getHourOfDay() < pod.getStartingHour() ? cur : cur.plusDays(1);
		startOfPod = startOfPod.withHourOfDay(pod.getStartingHour()).withMinuteOfHour(0).withSecondOfMinute(0);
		return new Period(cur, startOfPod, PeriodType.seconds()).getSeconds();
	}
	
	public double getLuminosity() {
		return luminosity;
	}
	
	void setLuminosity(double luminosity) {
		if (luminosity < 0.0 || luminosity > 1.0) {
			throw new IllegalArgumentException("luminosity must be nonnegative and not bigger than 1.");
		}
		this.luminosity = luminosity;
	}
	
	public int getStartingHour() {
		return startingHour;
	}
	
	public void setStartingHour(int startingHour) {
		if (startingHour < 0 || startingHour > 23) {
			throw new IllegalArgumentException("startingHour must be in the range [0, 23]");
		}
		this.startingHour = startingHour;
	}
	
	@Override
	public String toString() {
		return stringRepresentation;
	}
}
