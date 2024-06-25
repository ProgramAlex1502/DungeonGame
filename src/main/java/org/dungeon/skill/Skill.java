package main.java.org.dungeon.skill;

import main.java.org.dungeon.game.Entity;
import main.java.org.dungeon.game.ID;

public class Skill extends Entity{
	private static final long serialVersionUID = 1L;
	
	public static final Skill FIREBALL = new Skill("FIREBALL", "Skill", "Fireball", 10, 6);
	public static final Skill BURNING_GROUND = new Skill("BURNING_GROUND", "Skill", "Burning Ground", 4, 6);
	private final int damage;
	private final int coolDown;
	private int remainingCoolDown;

	public Skill(String id, String type, String name, int damage, int coolDown) {
		super(new ID(id), type, name);
		this.damage = damage;
		this.coolDown = coolDown;
	}
	
	public int getDamageAndStartCoolDown() {
		remainingCoolDown = coolDown;
		return damage;
	}
	
	public boolean isReady() {
		return remainingCoolDown == 0;
	}
	
	void refresh() {
		if (remainingCoolDown > 0) {
			remainingCoolDown--;
		}
	}
	
	void reset() {
		remainingCoolDown = 0;
	}

}
