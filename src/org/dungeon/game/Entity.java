package org.dungeon.game;

import java.io.Serializable;

import org.dungeon.util.Percentage;

public abstract class Entity implements Selectable, Serializable {
	private static final long serialVersionUID = 1L;
	
	protected final String type;
	protected final Name name;
	protected final Weight weight;
	private final ID id;
	private final Percentage visibility;
	
	protected Entity(ID id, String type, Name name, Weight weight) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.weight = weight;
		this.visibility = new Percentage(0.3);
	}
	
	public ID getID() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public Name getName() {
		return name;
	}
	
	public Weight getWeight() {
		return weight;
	}
	
	public Percentage getVisibility() {
		return visibility;
	}
	
}
