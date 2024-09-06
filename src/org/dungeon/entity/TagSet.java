package org.dungeon.entity;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

import org.dungeon.io.DLogger;

public class TagSet<E extends Enum<E>> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Set<E> set;
	
	private TagSet(Set<E> set) {
		this.set = set;
	}
	
	public static <E extends Enum<E>> TagSet<E> makeEmptyTagSet(Class<E> enumClass) {
		return new TagSet<E>(EnumSet.noneOf(enumClass));
	}
	
	public static <E extends Enum<E>> TagSet<E> copyTagSet(TagSet<E> tagSet) {
		return new TagSet<E>(EnumSet.copyOf(tagSet.set));
	}
	
	public boolean hasTag(E tag) {
		return set.contains(tag);
	}
	
	public void addTag(E tag) {
		if (!set.add(tag)) {
			DLogger.warning("Tried to add a Tag that was already in the TagSet!");
		}
	}

}
