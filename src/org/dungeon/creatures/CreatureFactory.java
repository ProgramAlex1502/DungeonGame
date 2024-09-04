package org.dungeon.creatures;

import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;

public class CreatureFactory {
	
	public static Creature makeCreature(ID id) {
		Creature model = GameData.getCreatureModels().get(id);
		if (model != null) {
			Game.getGameState().getStatistics().getWorldStatistics().addSpawn(model.getName());
			return new Creature(model);
		} else {
			return null;
		}
	}

}
