package main.java.org.dungeon.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.util.SelectionResult;
import main.java.org.dungeon.util.Utils;

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
	
	public abstract boolean addItem(Item item);

	public abstract void removeItem(Item item);
	
	public Item findItem(String[] tokens) {
		SelectionResult<Item> selectionResult = Utils.selectFromList(items, tokens);
		
		if (selectionResult.size() == 0) {
			IO.writeString("Item not found.");
		} else if (selectionResult.size() == 1 || selectionResult.getDifferentNames() == 1) {
			return selectionResult.getMatch(0);
		} else {
			Utils.printAmbiguousSelectionMessage();
		}
		
		return null;
	}

}
