package org.dungeon.entity.items;

import org.dungeon.entity.Preset;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.Visibility;
import org.dungeon.entity.Weight;
import org.dungeon.entity.items.Item.Tag;
import org.dungeon.game.ID;
import org.dungeon.game.Name;

public final class ItemBlueprint implements Preset {
	
	final TagSet<Item.Tag> tagSet = TagSet.makeEmptyTagSet(Item.Tag.class);
	
	int maxIntegrity;
	int curIntegrity;
	
	int damage;
	double hitRate;
	int integrityDecrementOnHit;
	
	int nutrition;
	int integrityDecrementOnEat;
	
	long putrefactionPeriod;
	
	String text;
	
	private ID id;
	private String type;
	private Name name;
	private Weight weight;

	private Visibility visibility;
	
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
	
	@Override
	public Weight getWeight() {
		return weight;
	}
	
	public void setWeight(Weight weight) {
		this.weight = weight;
	}
	
	@Override
	public Visibility getVisibility() {
		return visibility;
	}
	
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
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
