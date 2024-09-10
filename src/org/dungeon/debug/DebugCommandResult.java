package org.dungeon.debug;

import org.dungeon.commands.CommandResult;

public class DebugCommandResult implements CommandResult {
	
	private final int duration;
	private final boolean modifiedTheGameState;
	
	public DebugCommandResult(int duration, boolean modifiedTheGameState) {
		this.duration = duration;
		this.modifiedTheGameState = modifiedTheGameState;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public boolean evaluateIfGameStateChanged() {
		return duration != 0 || modifiedTheGameState;
	}

}
