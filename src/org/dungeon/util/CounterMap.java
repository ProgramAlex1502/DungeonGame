package org.dungeon.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class CounterMap<K> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final HashMap<K, Integer> map = new HashMap<K, Integer>();
	
	public CounterMap() {
	}
	
	public Set<K> keySet() {
		return map.keySet();
	}
	
	public boolean isNotEmpty() {
		return !map.isEmpty();
	}
	
	public void incrementCounter(K key) {
		incrementCounter(key, 1);
	}
	
	public void incrementCounter(K key, Integer amount) {
		Integer counter = map.get(key);
		if (counter == null) {
			counter = amount;
		} else {
			counter = counter + amount;
		}
		map.put(key, counter);
	}
	
	public int getCounter(K key) {
		Integer counter = map.get(key);
		if (counter == null) {
			return 0;
		} else {
			return counter;
		}
	}
	
	@Override
	public String toString() {
		return String.format("CounterMap{map=%s}", map);
	}

}
