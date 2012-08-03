package org.trinity.foundation.shared.geometry.api;

public class Coordinate {

	private final int x, y;

	public Coordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(final Coordinate coordinates) {
		this(coordinates.getX(), coordinates.getY());
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
