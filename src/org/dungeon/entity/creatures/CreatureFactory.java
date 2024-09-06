package org.dungeon.entity.creatures;

import java.util.Map;

import org.dungeon.date.Date;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.entity.items.CreatureInventory.AdditionResult;
import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;
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
			giveItems(creature);
			return creature;
		} else {
			return null;
		}
	}
	
	public static Hero makeHero() {
		Hero hero = new Hero(creaturePresetMap.get(Constants.HERO_ID));
		giveItems(hero);
		return hero;
	}
	
	private static void giveItems(Creature creature) {
		CreaturePreset preset = creaturePresetMap.get(creature.getID());
		for (ID itemID : preset.getItems()) {
			Date date = Game.getGameState().getWorld().getWorldDate();
			AdditionResult result = creature.getInventory().addItem(ItemFactory.makeItem(itemID, date));
			if (result != AdditionResult.SUCCESSFUL) {
				DLogger.warning("Could not add " + itemID + " to " + creature.getID() + "! Got " + result + ".");
			}
		}
		equipWeapon(creature, preset);
	}
	
	private static void equipWeapon(Creature creature, CreaturePreset preset) {
		if (preset.getWeaponID() != null) {
			for (Item item : creature.getInventory().getItems()) {
				if (item.getID().equals(preset.getWeaponID())) {
					creature.setWeapon(item);
					break;
				}
			}
			if (!creature.hasWeapon()) {
				DLogger.warning(String.format("%s not found in the inventory of %s!", preset.getWeaponID(), preset.getID()));
			}
		}
	}

}
