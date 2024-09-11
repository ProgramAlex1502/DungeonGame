package org.dungeon.util;

import java.util.ArrayList;
import java.util.Collections;

import org.dungeon.game.Random;
import org.dungeon.io.DLogger;

public final class ShuffledRange {
	
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
	
	public void shuffle() {
		int lastIntegerBeforeShuffling = integers.get(integers.size() - 1);
		Collections.shuffle(integers);
		if (getSize() > 1 && get(0) == lastIntegerBeforeShuffling) {
			Collections.swap(integers, 0, 1 + Random.nextInteger(getSize() - 1));
		}
	}

}
