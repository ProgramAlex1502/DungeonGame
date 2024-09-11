package org.dungeon.entity.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dungeon.entity.items.Item.Tag;

public abstract class BaseInventory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	final List<Item> items;
	
	BaseInventory() {
		items = new ArrayList<Item>();
	}
	
	private static boolean isDecomposed(Item item) {
		return (item.hasTag(Tag.DECOMPOSES) && item.getAge() >= item.getDecompositionPeriod());
	}
	
	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	public int getItemCount() {
		return items.size();
	}
	
	public boolean hasItem(Item item) {
		return items.contains(item);
	}
	
	protected abstract void removeItem(Item item);
	
	public void refreshItems() {
		for (Item item : new ArrayList<Item>(items)) {
			if (isDecomposed(item)) {
				removeItem(item);	
			}
		}
	}

}
