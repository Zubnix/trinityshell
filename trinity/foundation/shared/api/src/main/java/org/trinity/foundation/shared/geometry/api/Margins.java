package org.trinity.foundation.shared.geometry.api;

public class Margins {

	private final int bottom, left, right, top;

	/*****************************************
	 * 
	 ****************************************/
	public Margins(final int margins) {
		this(	margins,
				margins);
	}

	public Margins(final int horiz, final int vert) {
		this(	horiz,
				horiz,
				vert,
				vert);
	}

	public Margins(final int left, final int right, final int bottom, final int top) {
		this.bottom = bottom;
		this.right = right;
		this.left = left;
		this.top = top;
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