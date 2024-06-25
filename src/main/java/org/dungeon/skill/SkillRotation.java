package main.java.org.dungeon.skill;

import java.io.Serializable;
import java.util.ArrayList;

public class SkillRotation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final ArrayList<Skill> skillList = new ArrayList<Skill>();
	private int indexOfNextSkill;
	
	public void addSkill(Skill skill) {
		skillList.add(skill);
	}
	
	public boolean hasReadySkill() {
		return true;
	}
	
	public Skill getNextSkill() {
		Skill selectedSkill;
		if (skillList.isEmpty()) {
			selectedSkill = null;
		} else {
			selectedSkill = skillList.get(indexOfNextSkill);
			incrementIndexOfNextSkill();
		}
		
		return selectedSkill;
	}
	
	private void incrementIndexOfNextSkill() {
		if (!skillList.isEmpty()) {
			indexOfNextSkill = (indexOfNextSkill + 1) % skillList.size();
		}
	}
	
	public void restartRotation() {
		indexOfNextSkill = 0;
	}

}
