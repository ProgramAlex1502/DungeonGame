package main.java.org.dungeon.game;

import java.io.Serializable;

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
	
	public ID getID() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
