package main.java.org.dungeon.items;

import java.awt.Color;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.utils.Constants;
import main.java.org.dungeon.utils.Utils;

public class CreatureInventory extends BaseInventory implements LimitedInventory {
	private static final long serialVersionUID = 1L;
	
	private final Creature owner;
	private int limit;
	
	public CreatureInventory(Creature owner, int limit) {
		this.owner = owner;
		this.limit = limit;
	}
	
	public void printItems() {
		if (items.size() == 0) {
			if (Engine.RANDOM.nextBoolean()) {
				IO.writeString("Inventory is empty.");
			} else {
				IO.writeString("There are no items in the inventory.");
			}
		} else {
			for (Item item : items) {
				printItem(item);
			}
		}
	}
	
	private void printItem(Item item) {
		Color color = item.isEquipped() ? Color.MAGENTA : Constants.FORE_COLOR_NORMAL;
		int typeTagLength = 10;
		String typeString = "[" + item.getType() + "]";
		String extraSpace = Utils.makeRepeatedCharacterString(typeTagLength - typeString.length(), ' ');
		
		IO.writeString(typeString, color, false);
		
		if (item.isPerfect()) {
			IO.writeString(extraSpace + item.getName(), color);
		} else {
			IO.writeString(extraSpace + item.getIntegrityString(), Constants.FORE_COLOR_DARKER, false);
			IO.writeString(" " + item.getName(), color);
		}
	}

	@Override
	public boolean addItem(Item newItem) {
		if (hasItem(newItem)) {
			throw new IllegalArgumentException("newItem is already in the inventory.");
		}
		if (isFull()) {
			IO.writeString(Constants.INVENTORY_FULL);
		} else {
			items.add(newItem);
			newItem.setOwner(owner);
			IO.writeString("Added " + newItem.getName() + " to the inventory.");
			return true;
		}
		return false;
	}

	@Override
	public void removeItem(Item item) {
		if(owner.getWeapon() == item) {
			owner.setWeapon(null);
		}
		items.remove(item);
		item.setOwner(null);
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isFull() {
		return this.items.size() == this.limit;
	}


}
