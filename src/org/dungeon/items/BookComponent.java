package org.dungeon.items;

import java.io.Serializable;

import org.dungeon.game.ID;

public class BookComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ID skillID;
	
	public BookComponent(ID skillID) {
		this.skillID = skillID;
	}
	
	public ID getSkillID() {
		return skillID;
	}

}
