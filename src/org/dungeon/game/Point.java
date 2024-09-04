package org.dungeon.game;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point originalPoint, Direction shift) {
		this.x = originalPoint.getX() + shift.getX();
		this.y = originalPoint.getY() + shift.getY();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Object anotherObject) {
		if (this == anotherObject) {
			return true;
		}
		if (anotherObject instanceof Point) {
			Point anotherPoint = (Point) anotherObject;
			return this.x == anotherPoint.getX() && this.y == anotherPoint.getY();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + this.x;
		hash = 29 * hash + this.y;
		return hash;
	}
	
	@Override
	public String toString() {
		return String.format("{%d,%d}", getX(), getY());
	}

}
