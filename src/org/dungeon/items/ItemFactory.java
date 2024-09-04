package org.dungeon.items;

import org.dungeon.date.Date;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;

public abstract class ItemFactory {
	
	public static Item makeItem(ID id, Date date) {
		ItemBlueprint blueprint = GameData.getItemBlueprints().get(id);
		if (blueprint != null) {
			return new Item(blueprint, date);
		} else {
			return null;
		}
	}

}
