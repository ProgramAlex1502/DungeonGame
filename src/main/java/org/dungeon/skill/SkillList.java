package main.java.org.dungeon.skill;

import java.io.Serializable;
import java.util.ArrayList;

public class SkillList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<Skill> list = new ArrayList<Skill>();
	
	public void addSkill(Skill skill) {
		list.add(skill);
	}
	
	public boolean hasSkill() {
		return !list.isEmpty();
	}
	
	public Skill getFirstSkill() {
		if (hasSkill()) {
			return list.get(0);
		}
		return null;
	}

}
