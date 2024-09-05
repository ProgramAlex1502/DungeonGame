package org.dungeon.creatures;

import java.util.ArrayList;
import java.util.List;

import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.game.TagSet;
import org.dungeon.game.Weight;
import org.dungeon.io.DLogger;

public final class CreaturePreset {
	
	private final TagSet<Creature.Tag> tagSet = TagSet.makeEmptyTagSet(Creature.Tag.class);
	private ID id;
	private String type;
	private Name name;
	private Weight weight;
	private int health;
	private int attack;
	private ID attackAlgorithmID;
	private List<ID> items = new ArrayList<ID>();
	
	private static int validate(int value, int minimum, String attributeName) {
		if (value >= minimum) {
			return value;
		} else {
			String s = String.format("Attempted to set %d to %s in CreaturePreset. Using %s.", value, attributeName, minimum);
			DLogger.warning(s);
			return minimum;
		}
	}
	
	TagSet<Creature.Tag> getTagSet() {
		return tagSet;
	}
	
	public boolean hasTag(Creature.Tag corpse) {
		return tagSet.hasTag(corpse);
	}
	
	public void addTag(Creature.Tag tag) {
		tagSet.addTag(tag);
	}
	
	public ID getID() {
		return id;
	}
	
	public void setID(ID id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Name getName() {
		return name;
	}
	
	public void setName(Name name) {
		this.name = name;
	}
	
	public Weight getWeight() {
		return weight;
	}
	
	public void setWeight(Weight weight) {
		this.weight = weight;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		health = validate(health, 1, "health");
		this.health = health;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public void setAttack(int attack) {
		attack = validate(attack, 0, "attack");
		this.attack = attack;
	}
	
	public ID getAttackAlgorithmID() {
		return attackAlgorithmID;
	}
	
	public void setAttackAlgorithmID(ID attackAlgorithmID) {
		this.attackAlgorithmID = attackAlgorithmID;
	}
	
	public List<ID> getItems() {
		return items;
	}
	
	public void setItems(List<ID> items) {
		this.items = items;
	}

}
