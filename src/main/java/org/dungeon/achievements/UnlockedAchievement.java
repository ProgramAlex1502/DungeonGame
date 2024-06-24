package main.java.org.dungeon.achievements;

import java.io.Serializable;

import org.joda.time.DateTime;

import main.java.org.dungeon.game.ID;

public class UnlockedAchievement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final ID id;
	public final String name;
	public final DateTime date;
	
	public UnlockedAchievement(ID id, String name, DateTime date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}

}
