package org.dungeon.items;

import org.dungeon.game.Weight;

interface LimitedInventory {
	
	public int getItemLimit();
	
	public Weight getWeightLimit();

}
