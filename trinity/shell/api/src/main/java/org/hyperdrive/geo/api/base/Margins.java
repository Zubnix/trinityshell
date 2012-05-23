package org.hyperdrive.geo.api.base;

public class Margins {
	private final int top, right, bottom, left;

	public Margins(final int margins) {
		this(margins, margins, margins, margins);
	}

	public Margins(final int horiz, final int vert) {
		this(vert, horiz, vert, horiz);
	}

	public Margins(final int top, final int right, final int bottom,
			final int left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public int getBottom() {
		return this.bottom;
	}

	public int getLeft() {
		return this.left;
	}

	public int getRight() {
		return this.right;
	}

	public int getTop() {
		return this.top;
	}
}