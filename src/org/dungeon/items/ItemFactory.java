package org.dungeon.items;

import org.dungeon.creatures.Creature;
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
	
	public static Item makeCorpse(Creature creature, Date date) {
		return makeItem(makeCorpseIDFromCreatureID(creature.getID()), date);
	}
	
	public static ID makeCorpseIDFromCreatureID(ID id) {
		return new ID(id + "_CORPSE");
	}

}
