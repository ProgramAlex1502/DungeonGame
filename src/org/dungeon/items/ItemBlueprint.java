package org.dungeon.items;

import java.util.Set;

import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.game.Weight;
import org.dungeon.items.Item.Tag;

public final class ItemBlueprint {
	
	ID id;
	String type;
	Name name;
	
	Weight weight;
	
	Set<Item.Tag> tags;
	
	int maxIntegrity;
	int curIntegrity;
	
	int damage;
	double hitRate;
	int integrityDecrementOnHit;
	
	int nutrition;
	int integrityDecrementOnEat;
	
	long putrefactionPeriod;
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
	
	public Name getName() {
		return name;
	}
	
	public void setName(Name name) {
		this.name = name;
	}
	
	public void setWeight(Weight weight) {
		this.weight = weight;
	}
	
	public Set<Item.Tag> getTags() {
		return tags;
	}
	
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	public void setMaxIntegrity(int maxIntegrity) {
		this.maxIntegrity = maxIntegrity;
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
