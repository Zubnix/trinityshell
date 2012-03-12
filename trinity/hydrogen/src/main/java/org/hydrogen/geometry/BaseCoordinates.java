package org.hydrogen.geometry;

import org.hydrogen.api.geometry.Coordinates;

public class BaseCoordinates implements Coordinates {

	private final int x, y;

	public BaseCoordinates(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public final int getX() {
		return this.x;
	}

	@Override
	public final int getY() {
		return this.y;
	}
}
