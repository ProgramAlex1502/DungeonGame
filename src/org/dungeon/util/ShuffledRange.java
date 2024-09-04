package org.dungeon.util;

import java.util.ArrayList;
import java.util.Collections;

import org.dungeon.game.Engine;
import org.dungeon.io.DLogger;

public class ShuffledRange {
	
	private ArrayList<Integer> integers;
	
	public ShuffledRange(int start, int end) {
		if (start >= end) {
			DLogger.warning("Tried to create a ShuffledRange of negative or zero length.");
		} else {
			integers = new ArrayList<Integer>(end - start - 1);
			for (int i = start; i < end; i++) {
				integers.add(i);
			}
			shuffle();
		}
	}
	
	public int get(int index) {
		return integers.get(index);
	}
	
	public int getSize() {
		return integers.size();
	}
	
	public int getLast() {
		return integers.get(integers.size() - 1);
	}
	
	public void shuffle() {
		int lastInteger = getLast();
		Collections.shuffle(integers);
		if (getSize() > 1 && get(0) == lastInteger) {
			Collections.swap(integers, 0, 1 + Engine.RANDOM.nextInt(getSize() - 1));
		}
	}

}
