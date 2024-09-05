package org.dungeon.items;

import java.util.Set;

import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.game.Engine;
import org.dungeon.game.Entity;
import org.dungeon.game.Game;
import org.dungeon.game.Weight;
import org.dungeon.util.Percentage;

public class Item extends Entity {
	private static final long serialVersionUID = 1L;
	
	private final Set<Tag> tags;
	private final int maxIntegrity;
	private final Date dateOfCreation;
	private final long decompositionPeriod;
	
	private int curIntegrity;
	private WeaponComponent weaponComponent;
	private FoodComponent foodComponent;
	private ClockComponent clockComponent;
	private BookComponent bookComponent;

	public Item(ItemBlueprint bp, Date date) {
		super(bp.id, bp.type, bp.name, bp.weight);
		
		tags = bp.tags;
		
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
			bookComponent = new BookComponent(bp.getSkill());
		}
	}
	
	@Override
	public Weight getWeight() {
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
	
	int getMaxIntegrity() {
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
		if (hasTag(Tag.CLOCK)) {
			clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
		}
	}
	
	public boolean hasTag(Tag tag) {
		return tags.contains(tag);
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
	
	public boolean isBroken() {
		return getCurIntegrity() == 0;
	}
	
	public void incrementIntegrity(int integrityIncrement) {
		setCurIntegrity(getCurIntegrity() + integrityIncrement);
	}
	
	public void decrementIntegrityByHit() {
		setCurIntegrity(getCurIntegrity() - weaponComponent.getIntegrityDecrementOnHit());
	}
	
	
	public void decrementIntegrity(int integrityDecrement) {
		setCurIntegrity(getCurIntegrity() - integrityDecrement);
	}
	
	public boolean rollForHit() {
		return Engine.roll(weaponComponent.getHitRate());
	}
	
	String getIntegrityString() {
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
