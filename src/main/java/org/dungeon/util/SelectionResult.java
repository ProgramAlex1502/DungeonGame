package main.java.org.dungeon.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.java.org.dungeon.game.Selectable;

public class SelectionResult<T extends Selectable> {
	
	private final List<T> matches;
	
	private int differentNames;
	private boolean differentNamesUpToDate;
	
	public SelectionResult() {
		matches = new ArrayList<T>();
		differentNames = 0;
		differentNamesUpToDate = true;
	}
	
	public void addMatch(T match) {
		matches.add(match);
		differentNamesUpToDate = false;
	}
	
	public T getMatch(int index) {
		return matches.get(index);
	}
	
	public boolean hasName(String name) {
		for (T match : matches) {
			if (match.getName().equals(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	public int size() {
		return matches.size();
	}
	
	public int getDifferentNames() {
		if (!differentNamesUpToDate) {
			updateDifferentNamesCount();
		}
		
		return differentNames;
	}
	
	private void updateDifferentNamesCount() {
		HashSet<String> uniqueNames = new HashSet<String>();
		for (T match : matches) {
			uniqueNames.add(match.getName());
		}
		
		differentNames = uniqueNames.size();
		differentNamesUpToDate = true;
	}

}
