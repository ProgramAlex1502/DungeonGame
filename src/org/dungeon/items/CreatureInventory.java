package org.dungeon.items;


import org.dungeon.creatures.Creature;
import org.dungeon.game.Weight;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;

public class CreatureInventory extends BaseInventory implements LimitedInventory {
	private static final long serialVersionUID = 1L;
	
	private final Creature owner;
	private final int itemLimit;
	private final Weight weightLimit;
	
	public CreatureInventory(Creature owner, int itemLimit, double weightLimit) {
		this.owner = owner;
		this.itemLimit = itemLimit;
		this.weightLimit = Weight.newInstance(weightLimit);
	}
	
	@Override
	public int getItemLimit() {
		return itemLimit;
	}
	
	@Override
	public Weight getWeightLimit() {
		return weightLimit;
	}
	
	public Weight getWeight() {
		Weight sum = Weight.ZERO;
		for (Item item : getItems()) {
			sum = sum.add(item.getWeight());
		}
		return sum;
	}
	
	public boolean addItem(Item item) {
		if (hasItem(item)) {
			DLogger.warning("Tried to add an item to a CreatureInventory that already has it.");
			return false;
		}
		
		if (isFull()) {
			IO.writeString("Your inventory is full.");
		} else if (willExceedWeightLimitAfterAdding(item)) {
			IO.writeString("You can't carry more weight.");
		} else {
			items.add(item);
			IO.writeString("Added " + item.getName() + " to the inventory.");
			return true;
		}
		
		return false;
	}
	
	private boolean isFull() {
		return getItemCount() == getItemLimit();
	}
	
	private boolean willExceedWeightLimitAfterAdding(Item item) {
		return getWeight().add(item.getWeight()).compareTo(getWeightLimit()) > 0;
	}
	
	public void removeItem(Item item) {
		if (owner.getWeapon() == item) {
			owner.setWeapon(null);
		}
		
		items.remove(item);
	}

}
