package org.dungeon.creatures;

import org.dungeon.date.Date;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;
import org.dungeon.items.CreatureInventory.AdditionResult;
import org.dungeon.items.ItemFactory;
import org.dungeon.util.Constants;

public abstract class CreatureFactory {
	
	public static Creature makeCreature(ID id) {
		CreaturePreset preset = GameData.getCreaturePresets().get(id);
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
		Hero hero = new Hero(GameData.getCreaturePresets().get(Constants.HERO_ID));
		giveItems(hero, Constants.HERO_ID);
		return hero;
	}
	
	private static void giveItems(Creature creature, ID id) {
		for (ID itemID : GameData.getCreaturePresets().get(id).getItems()) {
			Date date = Game.getGameState().getWorld().getWorldDate();
			AdditionResult result = creature.getInventory().addItem(ItemFactory.makeItem(itemID, date));
			if (result != AdditionResult.SUCCESSFUL) {
				DLogger.warning("Could not add " + itemID + " to " + id + "! Got " + result + ".");
			}
		}
	}

}
