package org.dungeon.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class CircularList<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int capacity;
	private final List<T> list;
	
	private int zeroIndex;
	
	public CircularList(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("capacity must be positive.");
		}
		this.capacity = capacity;
		list = new ArrayList<T>(capacity);
	}
	
	public void add(T t) {
		if (isFull()) {
			list.set(zeroIndex, t);
			incrementZeroIndex();
		} else {
			list.add(t);
		}
	}
	
	private void incrementZeroIndex() {
		zeroIndex = (zeroIndex + 1) % capacity;
	}
	
	public int size() {
		return list.size();
	}
	
	protected boolean isFull() {
		return size() == capacity;
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public T get(final int index) {
		if (index > size()) {
			throw new IndexOutOfBoundsException();
		}
		return list.get((index + zeroIndex) % capacity);
	}

}
