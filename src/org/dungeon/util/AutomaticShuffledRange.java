package org.dungeon.util;

public class AutomaticShuffledRange {
	
	private final ShuffledRange shuffledRange;
	private int index;
	
	public AutomaticShuffledRange(int start, int end) {
		shuffledRange = new ShuffledRange(start, end);
	}
	
	public int getNext() {
		int value = shuffledRange.get(index);
		index++;
		if (index == shuffledRange.getSize()) {
			index = 0;
			shuffledRange.shuffle();
		}
		return value;
	}

}
