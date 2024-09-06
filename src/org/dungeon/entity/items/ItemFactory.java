package org.dungeon.entity.items;

import org.dungeon.date.Date;
import org.dungeon.entity.creatures.Creature;
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
		if (!creature.hasTag(Creature.Tag.CORPSE)) {
			throw new AssertionError("Called makeCorpse for Creature that does not have the CORPSE tag!");
		}
		return makeItem(makeCorpseIDFromCreatureID(creature.getID()), date);
	}
	
	public static ID makeCorpseIDFromCreatureID(ID id) {
		return new ID(id + "_CORPSE");
	}

}
