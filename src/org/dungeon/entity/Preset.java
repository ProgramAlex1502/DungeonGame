package org.dungeon.entity;

import org.dungeon.game.ID;
import org.dungeon.game.Name;

public interface Preset {
	
	ID getID();
	
	String getType();
	
	Name getName();
	
	Weight getWeight();
	
	Visibility getVisibility();

}
