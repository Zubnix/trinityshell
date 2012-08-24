package org.trinity.foundation.shared.geometry.api;

public class ImmutableRectangle implements Rectangle {

	private final int x, y, width, height;

	public ImmutableRectangle(final Rectangle rectangle) {
		this(	rectangle.getX(),
				rectangle.getY(),
				rectangle.getWidth(),
				rectangle.getHeight());
	}

	public ImmutableRectangle(final Coordinate position, final int width, final int height) {
		this(	position.getX(),
				position.getY(),
				width,
				height);
	}

	public ImmutableRectangle(final int x, final int y, final int width, final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
}
