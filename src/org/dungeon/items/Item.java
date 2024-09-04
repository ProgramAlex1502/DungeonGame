package org.dungeon.items;

import java.util.Set;

import org.dungeon.game.Engine;
import org.dungeon.game.Entity;
import org.dungeon.game.Game;
import org.dungeon.game.Weight;
import org.dungeon.util.Percentage;

public class Item extends Entity {
	private static final long serialVersionUID = 1L;
	
	public enum Tag { WEAPON, FOOD, CLOCK, BOOK, REPAIRABLE, WEIGHT_PROPORTIONAL_TO_INTEGRITY }
	
	private final Set<Tag> tags;
	private final int maxIntegrity;
	
	private final Weight weight;

	private int curIntegrity;
	private WeaponComponent weaponComponent;
	private FoodComponent foodComponent;
	private ClockComponent clockComponent;
	private BookComponent bookComponent;

	public Item(ItemBlueprint bp) {
		super(bp.id, bp.type, bp.name);
		
		tags = bp.tags;
		
		weight = bp.weight;
		
		maxIntegrity = bp.maxIntegrity;
		curIntegrity = bp.curIntegrity;
		
		if (hasTag(Tag.WEAPON)) {
			weaponComponent = new WeaponComponent(bp.damage, bp.hitRate, bp.integrityDecrementOnHit);
		}
		
		if (hasTag(Tag.FOOD)) {
			foodComponent = new FoodComponent(bp.nutrition, bp.integrityDecrementOnEat);
		}
		
		if (hasTag(Tag.CLOCK)) {
			clockComponent = new ClockComponent();
			clockComponent.setMaster(this);
		}
		
		if (hasTag(Tag.BOOK)) {
			bookComponent = new BookComponent(bp.getSkill());
		}
	}
	
	public Weight getWeight() {
		if (hasTag(Tag.WEIGHT_PROPORTIONAL_TO_INTEGRITY)) {
			Percentage integrityPercentage = new Percentage(curIntegrity / (double) maxIntegrity);
			return weight.multiply(integrityPercentage);
		} else {
			return weight;
		}
	}
	
	public String getQualifiedName() {
		if (getCurIntegrity() == getMaxIntegrity()) {
			return getName();
		} else {
			return getIntegrityString() + " " + getName();
		}
	}
	
	int getMaxIntegrity() {
		return maxIntegrity;
	}
	
	public int getCurIntegrity() {
		return curIntegrity;
	}
	
	public void setCurIntegrity(int curIntegrity) {
		if (curIntegrity > 0) {
			this.curIntegrity = curIntegrity;
		} else {
			this.curIntegrity = 0;
			
			if (hasTag(Tag.CLOCK)) {
				clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
			}
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
		setCurIntegrity(Math.min(getCurIntegrity() + integrityIncrement, getMaxIntegrity()));
	}
	
	public void decrementIntegrityByHit() {
		setCurIntegrity(getCurIntegrity() - weaponComponent.getIntegrityDecrementOnHit());
	}
	
	
	public void decrementIntegrity(int integrityDecrement) {
		setCurIntegrity(getCurIntegrity() - integrityDecrement);
	}
	
	public boolean rollForHit() {
		return weaponComponent.getHitRate() > Engine.RANDOM.nextDouble();
	}
	
	String getIntegrityString() {
		String weaponIntegrity;
		
		if (getCurIntegrity() == getMaxIntegrity()) {
			weaponIntegrity = "";
		} else if (getCurIntegrity() >= getMaxIntegrity() * 0.65) {
			weaponIntegrity = "Slightly Damaged";
		} else if (getCurIntegrity() >= getMaxIntegrity() * 0.3) {
			weaponIntegrity = "Damaged";
		} else if (getCurIntegrity() > 0) {
			weaponIntegrity = "Severely Damaged";
		} else {
			weaponIntegrity = "Broken";
		}
		
		return weaponIntegrity;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
