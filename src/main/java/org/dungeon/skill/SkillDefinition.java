package main.java.org.dungeon.skill;

import java.io.Serializable;

import main.java.org.dungeon.game.Entity;
import main.java.org.dungeon.game.ID;

public final class SkillDefinition extends Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	public final int damage;
	public final int coolDown;
	
	public SkillDefinition(String id, String type, String name, int damage, int coolDown) {
		super(new ID(id), type, name);
		this.damage = damage;
		this.coolDown = coolDown;
	}

}
