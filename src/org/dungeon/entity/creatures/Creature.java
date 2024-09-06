package org.dungeon.entity.creatures;

import java.util.Iterator;

import org.dungeon.entity.Entity;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.items.CreatureInventory;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.CreatureInventory.AdditionResult;
import org.dungeon.game.ID;
import org.dungeon.game.Location;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.skill.SkillList;
import org.dungeon.skill.SkillRotation;
import org.dungeon.stats.CauseOfDeath;

public class Creature extends Entity {
	private static final long serialVersionUID = 1L;

	private final int maxHealth;
	
	private final int attack;
	private final ID attackAlgorithmID;

	private final SkillList skillList = new SkillList();
	private final SkillRotation skillRotation = new SkillRotation();
	private final TagSet<Tag> tagSet;
	private int curHealth;
	private CreatureInventory inventory = new CreatureInventory(this, 4, 8);
	private Item weapon;
	
	private Location location;
	
	public Creature(CreaturePreset preset) {
		super(preset);
		maxHealth = preset.getHealth();
		curHealth = preset.getHealth();
		attack = preset.getAttack();
		tagSet = TagSet.copyTagSet(preset.getTagSet());
		attackAlgorithmID = preset.getAttackAlgorithmID();
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
	
	void setInventory(CreatureInventory inventory) {
		this.inventory = inventory;
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
	
	public boolean addItem(Item item) {
		AdditionResult result = getInventory().addItem(item);
		switch (result) {
		case AMOUNT_LIMIT:
			IO.writeString("Your inventory is full.");
			break;
		case WEIGHT_LIMIT:
			IO.writeString("You can't carry more weight.");
			break;
		case SUCCESSFUL:
			IO.writeString("Added " + item.getName() + " to the inventory.");
			return true;
		default:
			break;
		}
		return false;
	}
	
	void dropItem(Item item) {
		getInventory().removeItem(item);
		getLocation().addItem(item);
	}
	
	public void dropEverything() {
		for (Iterator<Item> iterator = getInventory().getItems().iterator(); iterator.hasNext();) {
			getLocation().addItem(iterator.next());
			iterator.remove();
		}
	}
	
	public ID getAttackAlgorithmID() {
		return attackAlgorithmID;
	}
	
	public enum Tag { MILKABLE, CORPSE }

}
