package org.dungeon.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class ExpandableIntegerSet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int MIN_DBI;
	private final int DIFF;
	
	private final TreeSet<Integer> set = new TreeSet<Integer>();
	
	public ExpandableIntegerSet(int MIN_DBI, int MAX_DBI) {
		if (MIN_DBI > 0 && MAX_DBI > MIN_DBI) {
			this.MIN_DBI = MIN_DBI;
			this.DIFF = MAX_DBI - MIN_DBI;
		} else {
			throw new IllegalArgumentException("illegal values for MIN_DBI or MAX_DBI");
		}
		initialize();
	}
	
	void initialize() {
		if (set.size() != 0) {
			throw new IllegalStateException("set already has an element.");
		} else {
			set.add(Engine.RANDOM.nextInt(MIN_DBI));
		}
	}
	
	List<Integer> expand(int a) {
		if (set.size() == 0) {
			throw new IllegalStateException("the set is empty.");
		}
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		int integer = set.last();
		while (a >= integer) {
			integer += MIN_DBI + Engine.RANDOM.nextInt(DIFF);
			integerList.add(integer);
			set.add(integer);
		}
		integer = set.first();
		while (a <= integer) {
			integer -= MIN_DBI + Engine.RANDOM.nextInt(DIFF);
			integerList.add(integer);
			set.add(integer);
		}
		return integerList;
	}
	
	boolean contains(int a) {
		return set.contains(a);
	}

}
