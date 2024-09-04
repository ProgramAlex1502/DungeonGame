package org.dungeon.creatures;

import org.dungeon.game.Entity;
import org.dungeon.game.ID;
import org.dungeon.game.Location;
import org.dungeon.game.Name;
import org.dungeon.items.CreatureInventory;
import org.dungeon.items.Item;
import org.dungeon.skill.SkillList;
import org.dungeon.skill.SkillRotation;
import org.dungeon.stats.CauseOfDeath;

public class Creature extends Entity {
	private static final long serialVersionUID = 1L;

	private final int maxHealth;
	
	private final int attack;
	private final String attackAlgorithm;

	private final SkillList skillList = new SkillList();
	private final SkillRotation skillRotation = new SkillRotation();
	private int curHealth;
	private CreatureInventory inventory;
	private Item weapon;
	
	private Location location;
	
	public Creature(ID id, String type, Name name, int health, int attack, String attackAlgorithm) {
		super(id, type, name);
		maxHealth =	curHealth = health;
		this.attackAlgorithm = attackAlgorithm;
		this.attack = attack;
	}
	
	Creature(Creature original) {
		this(original.getID(), original.type, original.name, original.maxHealth, original.attack, original.attackAlgorithm);
	}
	
	SkillList getSkillList() {
		return skillList;
	}
	
	public SkillRotation getSkillRotation() {
		return skillRotation;
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
	
	String getAttackAlgorithm() {
		return attackAlgorithm;
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
		this.weapon = weapon;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	void addHealth(int amount) {
		int sum = amount + getCurHealth();
		if (sum > getMaxHealth()) {
			setCurHealth(getMaxHealth());
		} else {
			setCurHealth(sum);
		}
	}
	
	public CauseOfDeath hit(Creature target) {
		return AttackAlgorithm.attack(this, target, getAttackAlgorithm());
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

}
