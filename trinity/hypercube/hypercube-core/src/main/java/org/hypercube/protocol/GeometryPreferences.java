package org.hypercube.protocol;

public class GeometryPreferences implements ProtocolEventArguments {
	private final int x, y, width, height, minWidth, minHeight, maxWidth,
			maxHeight, widthInc, heightInc;
	private final boolean visible;
	private final boolean resizable;

	public GeometryPreferences(final int x, final int y, final int width,
			final int height, final int minWidth, final int minHeight,
			final int maxWidth, final int maxHeight, final int widthInc,
			final int heightInc, final boolean visible, final boolean resizable) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.widthInc = widthInc;
		this.heightInc = heightInc;
		this.visible = visible;
		this.resizable = resizable;
	}

	public int getMinWidth() {
		return this.minWidth;
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public int getMaxWidth() {
		return this.maxWidth;
	}

	public int getMaxHeight() {
		return this.maxHeight;
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

	public boolean isVisible() {
		return this.visible;
	}

	public boolean isResizable() {
		return this.resizable;
	}

	public int getWidthInc() {
		return this.widthInc;
	}

	public int getHeightInc() {
		return this.heightInc;
	}
}
