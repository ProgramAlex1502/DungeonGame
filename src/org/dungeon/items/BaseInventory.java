package org.dungeon.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseInventory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	final List<Item> items;
	
	BaseInventory() {
		items = new ArrayList<Item>();
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public int getItemCount() {
		return items.size();
	}
	
	boolean hasItem(Item itemObject) {
		return items.contains(itemObject);
	}

}
