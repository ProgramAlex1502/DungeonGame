package main.java.org.dungeon.game;

import java.io.Serializable;

import org.joda.time.DateTime;

public class World implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//TODO: finish World class

	private DateTime worldDate;
	
	public DateTime getWorldDate() {
		return worldDate;
	}
	
	public PartOfDay getPartOfDay() {
		return PartOfDay.getCorrespondingConstants(new DateTime(worldDate));
	}

}
