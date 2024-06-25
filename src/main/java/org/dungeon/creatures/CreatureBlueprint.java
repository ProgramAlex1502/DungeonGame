package main.java.org.dungeon.creatures;

import main.java.org.dungeon.game.ID;

public class CreatureBlueprint {
	
	private ID id;
	private String type;
	private String name;
	private int curHealth;
	private int maxHealth;
	private int attack;
	private String attackAlgorithmID;
	
	public CreatureBlueprint() {
		
	}
	
	public ID getID() {
		return id;
	}
	
	public void setID(ID id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCurHealth() {
		return curHealth;
	}
	
	public void setCurHealth(int curHealth) {
		this.curHealth = curHealth;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public String getAttackAlgorithmID() {
		return attackAlgorithmID;
	}
	
	public void setAttackAlgorithmID(String attackAlgorithmID) {
		this.attackAlgorithmID = attackAlgorithmID;
	}

}
