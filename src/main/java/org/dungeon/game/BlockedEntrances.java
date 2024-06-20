package main.java.org.dungeon.game;

import java.util.HashSet;

public class BlockedEntrances {
	
	private HashSet<Direction> blockedEntrances;
	
	public BlockedEntrances() {
		
	}
	
	public BlockedEntrances(BlockedEntrances source) {
		if (source.blockedEntrances != null) {
			blockedEntrances = new HashSet<Direction>(source.blockedEntrances);
		}
	}
	
	public void block(Direction direction) {
		if (blockedEntrances == null) {
			blockedEntrances = new HashSet<Direction>();
		}
		
		blockedEntrances.add(direction);
	}
	
	public boolean isBlocked(Direction direction) {
		return blockedEntrances != null && blockedEntrances.contains(direction);
	}

}
