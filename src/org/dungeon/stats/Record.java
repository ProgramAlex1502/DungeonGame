package org.dungeon.stats;

import java.io.Serializable;

class Record implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Type type;
	private Integer value;
	
	public Record(Type type) {
		this.type = type;
	}
	
	public void update(int value) {
		if (this.value == null) {
			this.value = value;
		} else {
			if (type == Type.MAXIMUM) {
				this.value = Math.max(this.value, value);
			} else if (type == Type.MINIMUM) {
				this.value = Math.min(this.value, value);
			}
		}
	}
	
	public Integer getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		if (getValue() == null) {
			return "N/A";
		} else {
			return getValue().toString();
		}
	}
	
	public enum Type {MAXIMUM, MINIMUM}
	
}
