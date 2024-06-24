package main.java.org.dungeon.game;

import main.java.org.dungeon.date.Date;
import main.java.org.dungeon.date.Period;

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
	
	public static PartOfDay getCorrespondingConstants(Date date) {
		long hour = date.getHour();
		
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
	
	public static int getSecondsToNext(Date cur, PartOfDay pod) {
		Date day = cur.getHour() < pod.getStartingHour() ? cur : cur.plusDays(1);
		day = new Date(day.getYear(), day.getMonth(), day.getDay(), pod.getStartingHour(), 0, 0);
		return (int) new Period(cur, day).getSeconds();
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
