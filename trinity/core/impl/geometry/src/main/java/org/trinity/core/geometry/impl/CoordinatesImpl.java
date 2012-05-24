package org.trinity.core.geometry.impl;

import org.trinity.core.geometry.api.Coordinates;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class CoordinatesImpl implements Coordinates {

	private final int x, y;

	@AssistedInject
	protected CoordinatesImpl(	@Assisted("x") final int x,
								@Assisted("y") final int y) {
		this.x = x;
		this.y = y;
	}

	@AssistedInject
	protected CoordinatesImpl(@Assisted final Coordinates coordinates) {
		this(coordinates.getX(), coordinates.getY());
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
