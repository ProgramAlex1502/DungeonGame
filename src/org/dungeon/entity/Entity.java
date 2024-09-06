package org.dungeon.entity;

import java.io.Serializable;

import org.dungeon.game.ID;
import org.dungeon.game.Name;
import org.dungeon.game.Selectable;

public abstract class Entity implements Selectable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String type;
	private final Name name;
	private final Weight weight;
	private final ID id;
	private final Visibility visibility;
	
	protected Entity(Preset preset) {
		this.id = preset.getID();
		this.type = preset.getType();
		this.name = preset.getName();
		this.weight = preset.getWeight();
		this.visibility = preset.getVisibility();
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
	
	protected Weight getWeight() {
		return weight;
	}
	
	public Visibility getVisibility() {
		return visibility;
	}
	
}
