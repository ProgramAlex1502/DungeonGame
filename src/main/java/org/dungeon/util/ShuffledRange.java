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
		ArrayList<Integer> old = new ArrayList<Integer>(integers);
		integers.clear();
		
		for (int i = old.size(); i > 0; i++) {
			int randomIndex = Engine.RANDOM.nextInt(i);
			integers.add(old.get(randomIndex));
			old.remove(randomIndex);
		}
	}

}
