package org.dungeon.achievements;

import org.dungeon.game.ID;
import org.dungeon.game.PartOfDay;
import org.dungeon.stats.BattleRecord;
import org.dungeon.stats.CauseOfDeath;

public class BattleStatisticsQuery {
	
	private ID id;
	private String type;
	private CauseOfDeath causeOfDeath;
	private PartOfDay partOfDay;
	
	public BattleStatisticsQuery() {
		
	}
	
	public void setID(ID id) {
		this.id = id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setCauseOfDeath(CauseOfDeath causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}
	
	public void setPartOfDay(PartOfDay partOfDay) {
		this.partOfDay = partOfDay;
	}
	
	public boolean matches(BattleRecord record) {
		return (id == null || id.equals(record.getID())) && (type == null || type.equals(record.getType())) &&
				(causeOfDeath == null || causeOfDeath.equals(record.getCauseOfDeath())) &&
				(partOfDay == null || partOfDay.equals(record.getPartOfDay()));
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		BattleStatisticsQuery that = (BattleStatisticsQuery) o;
		
		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}
		if (type != null ? !type.equals(that.type) : that.type != null) {
			return false;
		}
		if (causeOfDeath != null ? !causeOfDeath.equals(that.causeOfDeath) : that.causeOfDeath != null) {
			return false;
		}
		return partOfDay == that.partOfDay;
	}
	
	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (causeOfDeath != null ? causeOfDeath.hashCode() : 0);
		result = 31 * result + (partOfDay != null ? partOfDay.hashCode() : 0);
		return result;
	}
	
	public String toString() {
		String format = "BattleStatisticsQuery{id=%s, type='%s', causeOfDeath=%s, partOfDay=%s";
		return String.format(format, id, type, causeOfDeath, partOfDay);
	}

}
