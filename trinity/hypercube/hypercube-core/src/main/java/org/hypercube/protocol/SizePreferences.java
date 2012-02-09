package org.hypercube.protocol;

public class SizePreferences implements ProtocolEventArguments {
	private final int x, y, width, height, minWidth, minHeight, maxWidth,
			maxHeight;

	public SizePreferences(final int x, final int y, final int width,
			final int height, final int minWidth, final int minHeight,
			final int maxWidth, final int maxHeight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
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
}
