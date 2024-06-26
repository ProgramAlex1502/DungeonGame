package main.java.org.dungeon.items;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.Entity;
import main.java.org.dungeon.game.Game;

public class Item extends Entity{
	private static final long serialVersionUID = 1L;

	
	private final int maxIntegrity;
	private final boolean repairable;	
	private final boolean weapon;
	private final int damage;
	private final double hitRate;
	private final int integrityDecrementOnHit;
	
	private Creature owner;
	private int curIntegrity;
	private FoodComponent foodComponent;	
	private ClockComponent clockComponent;
	private BookComponent bookComponent;
	
	public Item(ItemBlueprint bp) {
		super(bp.id, bp.type, bp.name);
		
		repairable = bp.repairable;
		maxIntegrity = bp.maxIntegrity;
		curIntegrity = bp.curIntegrity;
		
		weapon = bp.weapon;
		damage = bp.damage;
		hitRate = bp.hitRate;
		integrityDecrementOnHit = bp.integrityDecrementOnHit;
		
		if (bp.food) {
			foodComponent = new FoodComponent(bp.nutrition, bp.integrityDecrementOnEat);
		}
		
		if (bp.clock) {
			clockComponent = new ClockComponent();
			clockComponent.setMaster(this);
		}
		
		if (bp.book) {
			bookComponent = new BookComponent(bp.getSkill());
		}
	}
	
	public void setOwner(Creature owner) {
		this.owner = owner;
	}
	
	public boolean isEquipped() {
		return owner != null && owner.getWeapon() == this;
	}
	
	public String getName() {
		return name;
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
			
			if (isClock()) {
				clockComponent.setLastTime(Game.getGameState().getWorld().getWorldDate());
			}
		}
	}
	
	public boolean isRepairable() {
		return repairable;
	}
	
	public boolean isWeapon() {
		return weapon;
	}
	
	public int getDamage() {
		return damage;
	}
	
	double getHitRate() {
		return hitRate;
	}
	
	int getIntegrityDecrementOnHit() {
		return integrityDecrementOnHit;
	}
	
	public boolean isFood() {
		return foodComponent != null;
	}
	
	public FoodComponent getFoodComponent() {
		return foodComponent;
	}
	
	public boolean isClock() {
		return clockComponent != null;
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
	
	public boolean isPerfect() {
		return getCurIntegrity() == getMaxIntegrity();
	}
	
	public void decrementIntegrityByHit() {
		setCurIntegrity(getCurIntegrity() - getIntegrityDecrementOnHit());
	}
	
	public void decrementIntegrity(int integrityDecrement) {
		setCurIntegrity(getCurIntegrity() - integrityDecrement);
	}
	
	public boolean rollForHit() {
		return getHitRate() > Engine.RANDOM.nextDouble();
	}
	
	String getIntegrityString() {
		String weaponIntegrity;
		
		if(getCurIntegrity() == getMaxIntegrity()) {
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
		return name;
	}

}
