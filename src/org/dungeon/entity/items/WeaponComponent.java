package org.dungeon.entity.items;

import java.io.Serializable;

public class WeaponComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int damage;
	private final double hitRate;
	private final int integrityDecrementOnHit;
	
	public WeaponComponent(int damage, double hitRate, int integrityDecrementOnHit) {
		this.damage = damage;
		this.hitRate = hitRate;
		this.integrityDecrementOnHit = integrityDecrementOnHit;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public double getHitRate() {
		return hitRate;
	}
	
	public int getIntegrityDecrementOnHit() {
		return integrityDecrementOnHit;
	}

}
