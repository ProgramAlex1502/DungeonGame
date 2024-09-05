package org.dungeon.creatures;

import java.util.Map;

import org.dungeon.date.Date;
import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;
import org.dungeon.items.CreatureInventory.AdditionResult;
import org.dungeon.items.ItemFactory;
import org.dungeon.util.Constants;

public abstract class CreatureFactory {
	
	private static Map<ID, CreaturePreset> creaturePresetMap;
	
	public static void setCreaturePresetMap(Map<ID, CreaturePreset> creaturePresetMap) {
		if (CreatureFactory.creaturePresetMap == null) {
			CreatureFactory.creaturePresetMap = creaturePresetMap;
		} else {
			throw new AssertionError("Tried to set the CreaturePreset Map a second time!");
		}
	}
	
	public static Creature makeCreature(ID id) {
		CreaturePreset preset = creaturePresetMap.get(id);
		if (preset != null) {
			Game.getGameState().getStatistics().getWorldStatistics().addSpawn(preset.getName().getSingular());
			Creature creature = new Creature(preset);
			giveItems(creature, id);
			return creature;
		} else {
			return null;
		}
	}
	
	public static Hero makeHero() {
		Hero hero = new Hero(creaturePresetMap.get(Constants.HERO_ID));
		giveItems(hero, Constants.HERO_ID);
		return hero;
	}
	
	private static void giveItems(Creature creature, ID id) {
		for (ID itemID : creaturePresetMap.get(id).getItems()) {
			Date date = Game.getGameState().getWorld().getWorldDate();
			AdditionResult result = creature.getInventory().addItem(ItemFactory.makeItem(itemID, date));
			if (result != AdditionResult.SUCCESSFUL) {
				DLogger.warning("Could not add " + itemID + " to " + id + "! Got " + result + ".");
			}
		}
	}

}
