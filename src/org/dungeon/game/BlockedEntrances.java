package org.dungeon.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.dungeon.io.DLogger;
import org.dungeon.util.Utils;

public class BlockedEntrances implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Set<Direction> setOfBlockedEntrances = new HashSet<Direction>();
	
	public BlockedEntrances() {
	}
	
	public BlockedEntrances(BlockedEntrances source) {
		setOfBlockedEntrances = new HashSet<Direction>(source.setOfBlockedEntrances);
	}
	
	public void block(Direction direction) {
		if (isBlocked(direction)) {
			DLogger.warning("Tried to block an already blocked direction!");
		} else {
			setOfBlockedEntrances.add(direction);
		}
	}
	
	public boolean isBlocked(Direction direction) {
		return setOfBlockedEntrances.contains(direction);
	}
	
	@Override
	public String toString() {
		return Utils.enumerate(Arrays.asList(setOfBlockedEntrances.toArray()));
	}

}
