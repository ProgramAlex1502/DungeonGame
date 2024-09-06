package org.dungeon.entity;

import java.io.Serializable;

import org.dungeon.util.Percentage;

public class Visibility implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Percentage value;
	
	public Visibility(Percentage value) {
		this.value = value;
	}
	
	public Percentage toPercentage() {
		return value;
	}

}
