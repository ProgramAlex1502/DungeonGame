package org.dungeon.items;

import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.game.TagSet;
import org.dungeon.game.Weight;
import org.dungeon.items.Item.Tag;

public final class ItemBlueprint {
	
	final TagSet<Item.Tag> tagSet = TagSet.makeEmptyTagSet(Item.Tag.class);
	ID id;
	String type;
	Name name;
	
	Weight weight;
	
	int maxIntegrity;
	int curIntegrity;
	
	int damage;
	double hitRate;
	int integrityDecrementOnHit;
	
	int nutrition;
	int integrityDecrementOnEat;
	
	long putrefactionPeriod;
	
	String text;
	
	private ID skill;
	
	public void setPutrefactionPeriod(long putrefactionPeriod) {
		this.putrefactionPeriod = putrefactionPeriod;
	}
	
	public void setSkill(ID skill) {
		this.skill = skill;
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
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Name getName() {
		return name;
	}
	
	public void setName(Name name) {
		this.name = name;
	}
	
	public void setWeight(Weight weight) {
		this.weight = weight;
	}
	
	public boolean hasTag(Item.Tag tag) {
		return tagSet.hasTag(tag);
	}
	
	public void addTag(Tag tag) {
		tagSet.addTag(tag);
	}
	
	public int getMaxIntegrity() {
		return maxIntegrity;
	}
	
	public void setMaxIntegrity(int maxIntegrity) {
		this.maxIntegrity = maxIntegrity;
	}
	
	public int getCurIntegrity() {
		return curIntegrity;
	}
	
	public void setCurIntegrity(int curIntegrity) {
		this.curIntegrity = curIntegrity;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setHitRate(double hitRate) {
		this.hitRate = hitRate;
	}
	
	public int getIntegrityDecrementOnHit() {
		return integrityDecrementOnHit;
	}
	
	public void setIntegrityDecrementOnHit(int integrityDecrementOnHit) {
		this.integrityDecrementOnHit = integrityDecrementOnHit;
	}
	
	public void setNutrition(int nutrition) {
		this.nutrition = nutrition;
	}
	
	public void setIntegrityDecrementOnEat(int integrityDecrementOnEat) {
		this.integrityDecrementOnEat = integrityDecrementOnEat;
	}
	
	public ID getSkill() {
		return skill;
	}
	
	public void setSkill(String skill) {
		this.skill = new ID(skill);
	}
	
}
