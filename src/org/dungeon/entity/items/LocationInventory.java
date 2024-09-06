package org.dungeon.entity.items;

public class LocationInventory extends BaseInventory {
	private static final long serialVersionUID = 1L;

	public void addItem(Item item) {
		items.add(item);
	}

	public void removeItem(Item item) {
		items.remove(item);
	}
	
}
