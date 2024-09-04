package org.dungeon.skill;

import java.awt.Color;
import java.io.Serializable;

import org.dungeon.creatures.Creature;
import org.dungeon.game.ID;
import org.dungeon.game.Selectable;
import org.dungeon.io.IO;
import org.dungeon.items.Item;
import org.dungeon.util.Constants;

public class Skill implements Selectable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final SkillDefinition definition;
	private int remainingCoolDown;

	public Skill(SkillDefinition definition) {
		this.definition = definition;
	}
	
	private static void printSkillCast(Skill skill, Creature caster, Creature target) {
		StringBuilder builder = new StringBuilder();
		builder.append(caster.getName()).append(" casted ").append(skill.getName());
		if (skill.getDamage() > 0) {
			builder.append(" and inflicted ").append(skill.getDamage()).append(" damage points to ").append(target.getName());
		}
		builder.append(".");
		IO.writeString(builder.toString(), caster.getID().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
	}
	
	public ID getID() {
		return definition.id;
	}
	
	public int getDamage() {
		return definition.damage;
	}
	
	public int getRepair()  {
		return definition.repair;
	}
	
	private void startCoolDown() {
		remainingCoolDown = definition.coolDown;
	}
	
	public void cast(Creature caster, Creature target) {
		target.takeDamage(getDamage());
		printSkillCast(this, caster, target);
		Item casterWeapon = caster.getWeapon();
		if (casterWeapon != null && casterWeapon.hasTag(Item.Tag.REPAIRABLE) && getRepair() > 0) {
			casterWeapon.incrementIntegrity(getRepair());
			IO.writeString(casterWeapon.getName() + " was repaired.");
		}
		startCoolDown();
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
	
	@Override
	public String getName() {
		return definition.name;
	}

}
