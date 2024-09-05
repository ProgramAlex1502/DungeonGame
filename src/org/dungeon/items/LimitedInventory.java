package org.dungeon.items;

import org.dungeon.game.Weight;

interface LimitedInventory {
	
	int getItemLimit();
	
	Weight getWeightLimit();

}
