package org.dungeon.items;

public class LocationInventory extends BaseInventory {
	private static final long serialVersionUID = 1L;

	public boolean addItem(Item item) {
		items.add(item);
		return true;
	}

	public void removeItem(Item item) {
		items.remove(item);
	}
	
}
