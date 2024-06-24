package main.java.org.dungeon.achievements;

import java.io.Serializable;

import main.java.org.dungeon.date.Date;
import main.java.org.dungeon.game.ID;

public class UnlockedAchievement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final ID id;
	public final String name;
	public final Date date;
	
	public UnlockedAchievement(ID id, String name, Date date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}

}
