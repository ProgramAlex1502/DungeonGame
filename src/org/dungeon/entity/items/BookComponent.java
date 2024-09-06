package org.dungeon.entity.items;

import java.io.Serializable;

import org.dungeon.game.ID;

public class BookComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ID skillID;
	private final String text;
	
	public BookComponent(ID skillID, String text) {
		if (text == null) {
			throw new AssertionError("Tried to create a BookComponent with null text!");
		}
		this.skillID = skillID;
		this.text = text;
	}
	
	public ID getSkillID() {
		return skillID;
	}
	
	public String getText() {
		return text;
	}

}
