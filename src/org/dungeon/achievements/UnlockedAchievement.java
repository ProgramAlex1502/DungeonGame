package org.dungeon.achievements;

import java.io.Serializable;

import org.dungeon.date.Date;
import org.dungeon.game.ID;

public final class UnlockedAchievement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final ID id;
	private final String name;
	private final String info;
	private final Date date;
	
	public UnlockedAchievement(Achievement achievement, Date date) {
		this.id = achievement.getID();
		this.name = achievement.getName();
		this.info = achievement.getInfo();
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return info;
	}
	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return "Unlocked " + name + " in " + date;
	}

}
