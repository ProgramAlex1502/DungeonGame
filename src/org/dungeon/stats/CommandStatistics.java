package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.game.IssuedCommand;

final class CommandStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int commands;
	private int chars;
	private int words;
	
	public void addCommand(IssuedCommand issuedCommand) {
		commands++;
		words += issuedCommand.getTokenCount();
		for (char c : issuedCommand.getStringRepresentation().toCharArray()) {
			if (!Character.isWhitespace(c)) {
				chars++;
			}
		}
	}
	
	public int getCommandCount() {
		return commands;
	}
	
	public int getChars() {
		return chars;
	}
	
	public int getWords() {
		return words;
	}

}
