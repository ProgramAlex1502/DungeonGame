package main.java.org.dungeon.creatures;

import main.java.org.dungeon.game.Entity;
import main.java.org.dungeon.game.Location;
import main.java.org.dungeon.items.CreatureInventory;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.skill.SkillList;
import main.java.org.dungeon.skill.SkillRotation;

public class Creature extends Entity{
	private static final long serialVersionUID = 1L;
	
	private final int maxHealth;
	private int curHealth;
	
	private final int attack;
	private final String attackAlgorithm;
	
	private final SkillList skillList = new SkillList();
	private final SkillRotation skillRotation = new SkillRotation();
	private CreatureInventory inventory;
	private Item weapon;
	
	private Location location;

	public Creature(CreatureBlueprint bp) {
		super(bp.getId(), bp.getType(), bp.getName());
		attackAlgorithm = bp.getAttackAlgorithmID();
		attack = bp.getAttack();
		maxHealth = bp.getMaxHealth();
		curHealth = bp.getCurHealth();
	}
	
	public SkillList getSkillList() {
		return skillList;
	}
	
	public SkillRotation getSkillRotation() {
		return skillRotation;
	}
		
	public String getType() {
		return type;
	}
	
	@Override
	public String getName() {
		return name;
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
	
	public void hit(Creature target) {
		AttackAlgorithm.attack(this, target, getAttackAlgorithm());
	}
	
	public void takeDamage(int damage) {
		if (damage > getCurHealth()) {
			setCurHealth(0);
		} else {
			setCurHealth(getCurHealth() - damage);
		}
	}
	
	public boolean isAlive() {
		return getCurHealth() > 0;
	}
	
	public boolean isDead() {
		return !isAlive();
	}
	
	boolean hasWeapon() {
		return getWeapon() != null;
	}

}
