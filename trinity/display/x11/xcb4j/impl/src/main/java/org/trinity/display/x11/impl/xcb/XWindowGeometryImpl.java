package org.trinity.display.x11.impl.xcb;

import org.trinity.display.x11.core.api.XWindowGeometry;

public class XWindowGeometryImpl implements XWindowGeometry {

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final int borderWidth;

	public XWindowGeometryImpl(	final int x,
								final int y,
								final int width,
								final int height,
								final int borderWidth) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.borderWidth = borderWidth;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getBorderWidth() {
		return this.borderWidth;
	}
}
