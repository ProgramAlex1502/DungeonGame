package org.dungeon.entity.creatures;

import java.util.Map;

import org.dungeon.date.Date;
import org.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemFactory;
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
	
	public static Hero makeHero(Date date) {
		Hero hero = new Hero(creaturePresetMap.get(Constants.HERO_ID));
		giveItems(hero, date);
		return hero;
	}
	
	private static void giveItems(Creature creature) {
		giveItems(creature, Game.getGameState().getWorld().getWorldDate());
	}
	
	private static void giveItems(Creature creature, Date date) {
		CreaturePreset preset = creaturePresetMap.get(creature.getID());
		for (ID itemID : preset.getItems()) {
			Item item = ItemFactory.makeItem(itemID, date);
			SimulationResult result = creature.getInventory().simulateItemAddition(item);
			if (result == SimulationResult.SUCCESSFUL) {
				creature.getInventory().addItem(item);
			} else {
				DLogger.warning("Could not add " + itemID + " to " + creature.getID() + ". Reason: " + result + ".");
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
