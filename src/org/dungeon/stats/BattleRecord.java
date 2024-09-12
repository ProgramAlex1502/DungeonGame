package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.game.ID;
import org.dungeon.game.PartOfDay;

public final class BattleRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ID id;
	private final String type;
	private final CauseOfDeath causeOfDeath;
	private final PartOfDay partOfDay;
	
	public BattleRecord(ID id, String type, CauseOfDeath causeOfDeath, PartOfDay partOfDay) {
		this.id = id;
		this.type = type;
		this.causeOfDeath = causeOfDeath;
		this.partOfDay = partOfDay;
	}
	
	public ID getID() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public CauseOfDeath getCauseOfDeath() {
		return causeOfDeath;
	}
	
	public PartOfDay getPartOfDay() {
		return partOfDay;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		BattleRecord that = (BattleRecord) o;
		
		return id.equals(that.id) && type.equals(that.type) && causeOfDeath.equals(that.causeOfDeath);
	}
	
	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + causeOfDeath.hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		String format = "BattleEntry{id=%s, type='%s', causeOfDeath=%s}";
		return String.format(format, id, type, causeOfDeath);
	}

}
