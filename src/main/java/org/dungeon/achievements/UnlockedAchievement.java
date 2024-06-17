package main.java.org.dungeon.achievements;

import java.io.Serializable;

import org.joda.time.DateTime;

public class UnlockedAchievement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final String id;
	public final String name;
	public final DateTime date;
	
	public UnlockedAchievement(String id, String name, DateTime date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}

}
