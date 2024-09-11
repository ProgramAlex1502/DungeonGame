package org.dungeon.entity.items;

import java.io.Serializable;

import org.dungeon.game.ID;

public class BookComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final int SECONDS_PER_CHARACTER = 1;
	private final ID skillID;
	private final String text;
	
	public BookComponent(ID skillID, String text) {
		if (text == null) {
			throw new AssertionError("Tried to create a BookComponent with null text!");
		}
		this.skillID = skillID;
		this.text = text;
	}
	
	public boolean isDidactic() {
		return skillID != null;
	}
	
	public ID getSkillID() {
		return skillID;
	}
	
	public String getText() {
		return text;
	}
	
	public int getTimeToRead() {
		return getText().length() * SECONDS_PER_CHARACTER;
	}
	
	@Override
	public String toString() {
		String representation = String.format("This book teaches %s.", isDidactic() ? skillID : "nothing");
		representation += " " + "Text: " + text;
		return representation;
	}

}
