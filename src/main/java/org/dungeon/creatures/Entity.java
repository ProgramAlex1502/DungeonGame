package main.java.org.dungeon.creatures;

import java.io.Serializable;

import main.java.org.dungeon.game.Selectable;

public abstract class Entity implements Selectable, Serializable{
	private static final long serialVersionUID = 1L;
	
	public final String id;
	public final String type;
	public final String name;
	
	public Entity(String id, String type, String name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
