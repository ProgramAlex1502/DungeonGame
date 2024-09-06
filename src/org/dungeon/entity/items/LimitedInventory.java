package org.dungeon.entity.items;

import org.dungeon.entity.Weight;

interface LimitedInventory {
	
	int getItemLimit();
	
	Weight getWeightLimit();

}
