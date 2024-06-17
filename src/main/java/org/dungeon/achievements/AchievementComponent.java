package main.java.org.dungeon.achievements;

import main.java.org.dungeon.creatures.Hero;

public abstract class AchievementComponent {
	
	abstract boolean isFulfilled(Hero hero);
	
}
