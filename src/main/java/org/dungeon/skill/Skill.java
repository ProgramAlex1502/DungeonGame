package main.java.org.dungeon.skill;

import java.io.Serializable;

import main.java.org.dungeon.game.Entity;

public class Skill extends Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final SkillDefinition definition;
	private int remainingCoolDown;

	public Skill(SkillDefinition definition) {
		super(definition.getID(), definition.getType(), definition.getName());
		this.definition = definition;
	}
	
	public int getDamage() {
		return definition.damage;
	}
	
	public void startCoolDown() {
		remainingCoolDown = definition.coolDown;
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
