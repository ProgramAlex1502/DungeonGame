package org.dungeon.entity.creatures;

import java.util.ArrayList;

import org.dungeon.entity.Entity;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.items.CreatureInventory;
import org.dungeon.entity.items.Item;
import org.dungeon.game.Location;
import org.dungeon.io.DLogger;
import org.dungeon.skill.SkillList;
import org.dungeon.skill.SkillRotation;
import org.dungeon.stats.CauseOfDeath;

public class Creature extends Entity {
	private static final long serialVersionUID = 1L;

	private final int maxHealth;
	
	private final int attack;
	private final AttackAlgorithmID attackAlgorithmID;

	private final SkillList skillList = new SkillList();
	private final SkillRotation skillRotation = new SkillRotation();
	private final TagSet<Tag> tagSet;
	private final CreatureInventory inventory;
	private int curHealth;
	private Item weapon;
	
	private Location location;
	
	public Creature(CreaturePreset preset) {
		super(preset);
		maxHealth = preset.getHealth();
		curHealth = preset.getHealth();
		attack = preset.getAttack();
		tagSet = TagSet.copyTagSet(preset.getTagSet());
		attackAlgorithmID = preset.getAttackAlgorithmID();
		inventory = new CreatureInventory(this, preset.getInventoryItemLimit(), preset.getInventoryWeightLimit());
	}
	
	SkillList getSkillList() {
		return skillList;
	}
	
	public SkillRotation getSkillRotation() {
		return skillRotation;
	}
	
	public boolean hasTag(Tag tag) {
		return tagSet.hasTag(tag);
	}
	
	int getMaxHealth() {
		return maxHealth;
	}
	
	int getCurHealth() {
		return curHealth;
	}
	
	void setCurHealth(int curHealth) {
		this.curHealth = curHealth;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public CreatureInventory getInventory() {
		return inventory;
	}
	
	public Item getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Item weapon) {
		if (inventory.hasItem(weapon)) {
			if (weapon.hasTag(Item.Tag.WEAPON)) {
				this.weapon = weapon;
			} else {
				DLogger.warning(String.format("Tried to equip %s (no WEAPON tag) on %s!", weapon.getName(), getName()));
			}
		} else {
			DLogger.warning("Tried to equip an Item that is not in the inventory of " + getName() + "!");
		}
	}
	
	public void unsetWeapon() {
		this.weapon = null;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public CauseOfDeath hit(Creature target) {
		return AttackAlgorithms.renderAttack(this, target);
	}
	
	public boolean takeDamage(int damage) {
		HealthState initialHealthState = HealthState.getHealthState(getCurHealth(), getMaxHealth());
		if (damage > getCurHealth()) {
			setCurHealth(0);
		} else {
			setCurHealth(getCurHealth() - damage);
		}
		HealthState finalHealthState = HealthState.getHealthState(getCurHealth(), getMaxHealth());
		return finalHealthState != initialHealthState;
	}
	
	public boolean isAlive() {
		return getCurHealth() > 0;
	}
	
	public boolean isDead() {
		return !isAlive();
	}
	
	public boolean hasWeapon() {
		return getWeapon() != null;
	}
	
	void dropItem(Item item) {
		getInventory().removeItem(item);
		getLocation().addItem(item);
	}
	
	public void dropEverything() {
		for (Item item : new ArrayList<Item>(getInventory().getItems())) {
			getInventory().removeItem(item);
			getLocation().addItem(item);
		}
	}
	
	public AttackAlgorithmID getAttackAlgorithmID() {
		return attackAlgorithmID;
	}
	
	@Override
	public String toString() {
		return getName().getSingular();
	}
	
	public enum Tag { MILKABLE, CORPSE }

}
