package org.dungeon.entity.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
		return items;
	}
	
	public int getItemCount() {
		return items.size();
	}
	
	public boolean hasItem(Item itemObject) {
		return items.contains(itemObject);
	}
	
	protected abstract void removeItem(Item item);
	
	public void refreshItems() {
		for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {
			Item item = iterator.next();
			if (isDecomposed(item)) {
				iterator.remove();
			}
		}
	}

}
