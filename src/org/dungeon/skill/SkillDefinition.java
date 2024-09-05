package org.dungeon.skill;

import java.io.Serializable;

import org.dungeon.game.ID;
import org.dungeon.game.Name;

public final class SkillDefinition implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final ID id;
	public final Name name;
	public final int damage;
	public final int repair;
	public final int coolDown;
	
	public SkillDefinition(String id, String name, int damage, int repair, int coolDown) {
		this.id = new ID(id);
		this.name = Name.newInstance(name);
		this.damage = damage;
		this.repair = repair;
		this.coolDown = coolDown;
	}

}
