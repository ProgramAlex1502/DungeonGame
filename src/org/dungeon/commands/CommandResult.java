package org.dungeon.commands;

public interface CommandResult {
	
	int getDuration();
	
	boolean evaluateIfGameStateChanged();

}
