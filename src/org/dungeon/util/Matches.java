package org.dungeon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.dungeon.game.Selectable;

public class Matches<T extends Selectable> {
	
	private final List<T> matches;
	
	private int differentNames;
	private boolean differentNamesUpToDate;
	
	public Matches() {
		matches = new ArrayList<T>();
		differentNames = 0;
		differentNamesUpToDate = true;
	}
	
	public static <T extends Selectable> Matches<T> fromCollection(Collection<T> collection) {
		Matches<T> newInstance = new Matches<T>();
		for (T t : collection) {
			newInstance.addMatch(t);
		}
		return newInstance;
	}
	
	public void addMatch(T match) {
		matches.add(match);
		differentNamesUpToDate = false;
	}
	
	public T getMatch(int index) {
		return matches.get(index);
	}
	
	public List<T> toList() {
		return new ArrayList<T>(matches);
	}
	
	public boolean hasMatchWithName(String name) {
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
