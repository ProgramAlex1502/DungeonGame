package main.java.org.dungeon.util;

import java.util.ArrayList;

import main.java.org.dungeon.game.Engine;

public class ShuffledRange {
	
	private ArrayList<Integer> integers;
	
	public ShuffledRange(int start, int end) {
		integers = new ArrayList<Integer>(end - start - 1);
		for (int i = start; i < end; i++) {
			integers.add(i);
		}
		shuffle();
	}
	
	public int get(int index) {
		return integers.get(index);
	}
	
	public int getSize() {
		return integers.size();
	}
	
	public void shuffle() {
		if (integers.size() > 1) {
			ArrayList<Integer> old = new ArrayList<Integer>(integers);
			integers.clear();
			integers.add(old.remove(Engine.RANDOM.nextInt(old.size() - 1)));
			for (int i = old.size(); i > 0; i--) {
				integers.add(old.remove(Engine.RANDOM.nextInt(i)));
			}
		}
	}

}
