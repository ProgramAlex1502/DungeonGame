package org.dungeon.entity.items;

import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.entity.Entity;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.Weight;
import org.dungeon.game.Game;
import org.dungeon.game.Random;
import org.dungeon.io.DLogger;
import org.dungeon.util.Percentage;

public final class Item extends Entity {
	private static final long serialVersionUID = 1L;
	
	private final int maxIntegrity;
	private final Date dateOfCreation;
	private final long decompositionPeriod;
	private final TagSet<Tag> tagSet;
	
	private int curIntegrity;
	private WeaponComponent weaponComponent;
	private FoodComponent foodComponent;
	private ClockComponent clockComponent;
	private BookComponent bookComponent;
	
	private BaseInventory inventory;

	public Item(ItemBlueprint bp, Date date) {
		super(bp);
		
		tagSet = TagSet.copyTagSet(bp.tagSet);
		
		dateOfCreation = date;
		decompositionPeriod = bp.putrefactionPeriod;
		
		maxIntegrity = bp.maxIntegrity;
		curIntegrity = bp.curIntegrity;
		
		if (hasTag(Tag.WEAPON)) {
			weaponComponent = new WeaponComponent(bp.damage, bp.hitRate, bp.integrityDecrementOnHit);
		}
		
		if (hasTag(Tag.FOOD)) {
			foodComponent = new FoodComponent(bp.nutrition, bp.integrityDecrementOnEat);
		}
		
		if (hasTag(Tag.CLOCK)) {
			clockComponent = new ClockComponent(this);
		}
		
		if (hasTag(Tag.BOOK)) {
			bookComponent = new BookComponent(bp.getSkill(), bp.text);
		}
	}
	
	@Override
	public Weight getWeight() {
		Weight weight = super.getWeight();
		if (hasTag(Tag.WEIGHT_PROPORTIONAL_TO_INTEGRITY)) {
			Percentage integrityPercentage = new Percentage(curIntegrity / (double) maxIntegrity);
			return weight.multiply(integrityPercentage);
		} else {
			return weight;
		}
	}
	
	public long getAge() {
		Period existence = new Period(dateOfCreation, Game.getGameState().getWorld().getWorldDate());
		return existence.getSeconds();
	}
	
	public String getQualifiedName() {
		String singularName = getName().getSingular();
		if (getCurIntegrity() == getMaxIntegrity()) {
			return singularName;
		} else {
			return getIntegrityString() + " " + singularName;
		}
	}
	
	private int getMaxIntegrity() {
		return maxIntegrity;
	}
	
	public int getCurIntegrity() {
		return curIntegrity;
	}
	
	public void setCurIntegrity(int curIntegrity) {
		if (curIntegrity <= 0) {
			setIntegrityToZero();
		} else {
			this.curIntegrity = Math.min(curIntegrity, maxIntegrity);
		}
	}
	
	private void setIntegrityToZero() {
		this.curIntegrity = 0;
		if (!hasTag(Tag.REPAIRABLE)) {
			inventory.removeItem(this);
			return;
		}
		if (hasTag(Tag.CLOCK)) {
			clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
		}
	}
	
	public boolean hasTag(Tag tag) {
		return tagSet.hasTag(tag);
	}
	
	public WeaponComponent getWeaponComponent() {
		return weaponComponent;
	}
	
	public FoodComponent getFoodComponent() {
		return foodComponent;
	}
	
	public ClockComponent getClockComponent() {
		return clockComponent;
	}
	
	public BookComponent getBookComponent() {
		return bookComponent;
	}
	
	public void setInventory(BaseInventory inventory) {
		this.inventory = inventory;
	}
	
	public boolean isBroken() {
		return getCurIntegrity() == 0;
	}
	
	public void incrementIntegrity(int integrityIncrement) {
		setCurIntegrity(getCurIntegrity() + integrityIncrement);
	}
	
	public void decrementIntegrityByHit() {
		decrementIntegrity(weaponComponent.getIntegrityDecrementOnHit());
	}
	
	public void decrementIntegrityByEat() {
		decrementIntegrity(foodComponent.getIntegrityDecrementOnEat());
	}
	
	public void decrementIntegrity(int decrement) {
		if (decrement <= 0) {
			DLogger.warning("Got nonpositive integrity decrement value for a " + getName() + "!");
			throw new IllegalArgumentException("Integrity decrement must be positive!");
		}
		if (isBroken()) {
			DLogger.warning("Attempted to decrement the integrity of an already broken " + getName() + "!");
		}
		
		setCurIntegrity(getCurIntegrity() - decrement);
	}
	
	public boolean rollForHit() {
		return Random.roll(weaponComponent.getHitRate());
	}
	
	private String getIntegrityString() {
		return IntegrityState.getIntegrityState(getCurIntegrity(), getMaxIntegrity()).toString();
	}
	
	public long getDecompositionPeriod() {
		return decompositionPeriod;
	}
	
	@Override
	public String toString() {
		return getName().toString();
	}
	
	public enum Tag { WEAPON, FOOD, CLOCK, BOOK, DECOMPOSES, REPAIRABLE, WEIGHT_PROPORTIONAL_TO_INTEGRITY }

}
