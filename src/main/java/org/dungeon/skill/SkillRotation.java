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
		for (Skill skill : skillList) {
			if (skill.isReady()) {
				return true;
			}
		}
		return false;
	}
	
	public Skill getNextSkill() {
		Skill selectedSkill;
		if (skillList.isEmpty() || !hasReadySkill()) {
			selectedSkill = null;
		} else {
			int indexOfSelectedSkill = indexOfNextSkill;
			selectedSkill = skillList.get(indexOfSelectedSkill);
			if (selectedSkill.isReady()) {				
				incrementIndexOfNextSkill();
			} else {
				do {
					indexOfSelectedSkill = (indexOfSelectedSkill + 1) % skillList.size();
					selectedSkill = skillList.get(indexOfSelectedSkill);
				} while (!selectedSkill.isReady());
			}
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
		for (Skill skill : skillList) {
			skill.reset();
		}
	}
	
	public void refresh() {
		for (Skill skill : skillList) {
			skill.refresh();
		}
	}
	
	public void resetRotation() {
		skillList.clear();
	}

}
