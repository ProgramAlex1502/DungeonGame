package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.game.ID;

public class CauseOfDeath implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final TypeOfCauseOfDeath type;
	public final ID id;
	
	public CauseOfDeath(TypeOfCauseOfDeath type, ID id) {
		this.type = type;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		CauseOfDeath that = (CauseOfDeath) o;
		
		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}
		return type == that.type;
	}
	
	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("%s : %s", type, id);
	}

}
