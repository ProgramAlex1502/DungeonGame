package main.java.org.dungeon.skill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.game.Selectable;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.IO;

public class SkillList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<Skill> skillList = new ArrayList<Skill>();
	
	public void addSkill(Skill skill) {
		if (hasSkill(skill.getID())) {
			DLogger.warning("Tried to add an already present Skill to a SkillList!");
		} else {
			skillList.add(skill);
		}
	}
	
	public boolean hasSkill(ID skillID) {
		for (Skill skill : skillList) {
			if (skillID.equals(skill.getID())) {
				return true;
			}
		}
		
		return false;
	}
	
	public int getSize() {
		return skillList.size();
	}
	
	public void printSkillList() {
		for (Skill skill : skillList) {
			IO.writeString(skill.getName());
		}
	}
	
	public List<Selectable> toListOfSelectable() {
		return new ArrayList<Selectable>(skillList);
	}

}
