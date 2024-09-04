package org.dungeon.achievements;

import java.io.Serializable;

import org.dungeon.date.Date;
import org.dungeon.game.ID;

public final class UnlockedAchievement implements Serializable {
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
