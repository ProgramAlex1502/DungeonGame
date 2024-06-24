package main.java.org.dungeon.creatures;

import java.io.Serializable;

import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.game.Selectable;

public abstract class Entity implements Selectable, Serializable{
	private static final long serialVersionUID = 1L;
	
	private final ID id;
	protected final String type;
	protected final String name;
	
	protected Entity(ID id, String type, String name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}
	
	public ID getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
