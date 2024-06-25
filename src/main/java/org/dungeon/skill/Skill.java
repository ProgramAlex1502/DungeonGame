package main.java.org.dungeon.skill;

import main.java.org.dungeon.game.Entity;
import main.java.org.dungeon.game.ID;

public class Skill extends Entity{
	private static final long serialVersionUID = 1L;
	
	public static final Skill FIREBALL = new Skill("FIREBALL", "Skill", "Fireball", 10);
	private final int damage;

	public Skill(String id, String type, String name, int damage) {
		super(new ID(id), type, name);
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}

}
