package org.dungeon.commands;

public class SimpleCommandResult implements CommandResult {
	
	private final int duration;
	
	public SimpleCommandResult(int duration) {
		if (duration < 0) {
			throw new IllegalArgumentException("Attempted to create a SimpleCommandResult with a negative duration!");
		}
		this.duration = duration;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public boolean evaluateIfGameStateChanged() {
		return duration != 0;
	}

}
