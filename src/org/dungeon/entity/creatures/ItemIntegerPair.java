package org.dungeon.entity.creatures;

import org.dungeon.entity.items.Item;

class ItemIntegerPair {
	
	private final Item item;
	private final Integer integer;
	
	ItemIntegerPair(Item item, Integer integer) {
		this.item = item;
		this.integer = integer;
	}
	
	public Item getItem() {
		return item;
	}
	
	public Integer getInteger() {
		return integer;
	}

}
