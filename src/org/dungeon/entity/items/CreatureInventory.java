package org.dungeon.entity.items;

import org.dungeon.entity.Weight;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.io.DLogger;

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
	
	public AdditionResult addItem(Item item) {
		if (hasItem(item)) {
			DLogger.warning("Tried to add an item to a CreatureInventory that already has it.");
			return AdditionResult.ALREADY_IN_THE_INVENTORY;
		}
		
		if (isFull()) {
			return AdditionResult.AMOUNT_LIMIT;
		} else if (willExceedWeightLimitAfterAdding(item)) {
			return AdditionResult.WEIGHT_LIMIT;
		} else {
			items.add(item);
			item.setInventory(this);
			return AdditionResult.SUCCESSFUL;
		}
	}
	
	private boolean isFull() {
		return getItemCount() == getItemLimit();
	}
	
	private boolean willExceedWeightLimitAfterAdding(Item item) {
		return getWeight().add(item.getWeight()).compareTo(getWeightLimit()) > 0;
	}
	
	public void removeItem(Item item) {
		if (owner.getWeapon() == item) {
			owner.unsetWeapon();
		}
		
		items.remove(item);
		item.setInventory(null);
	}
	
	public enum AdditionResult { ALREADY_IN_THE_INVENTORY, AMOUNT_LIMIT, WEIGHT_LIMIT, SUCCESSFUL }

}
