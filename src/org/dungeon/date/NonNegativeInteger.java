package org.dungeon.date;

class NonNegativeInteger {
	
	private final Integer integer;
	
	public NonNegativeInteger(Integer integer) {
		if (integer < 0) {
			throw new IllegalArgumentException("integer must be nonnegative.");
		}
		this.integer = integer;
	}
	
	public Integer toInteger() {
		return integer;
	}
	
	@Override
	public String toString() {
		return integer.toString();
	}

}
