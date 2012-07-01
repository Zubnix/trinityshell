package org.trinity.foundation.shared.geometry.impl;

import org.trinity.foundation.shared.geometry.api.Margins;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class MarginsImpl implements Margins {
	private final int top, right, bottom, left;

	@AssistedInject
	public MarginsImpl(@Assisted final int margins) {
		this(	margins,
				margins,
				margins,
				margins);
	}

	@AssistedInject
	public MarginsImpl(	@Assisted("horiz") final int horiz,
						@Assisted("vert") final int vert) {
		this(	vert,
				horiz,
				vert,
				horiz);
	}

	@AssistedInject
	public MarginsImpl(	@Assisted("top") final int top,
						@Assisted("right") final int right,
						@Assisted("bottom") final int bottom,
						@Assisted("left") final int left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	@Override
	public int getBottom() {
		return this.bottom;
	}

	@Override
	public int getLeft() {
		return this.left;
	}

	@Override
	public int getRight() {
		return this.right;
	}

	@Override
	public int getTop() {
		return this.top;
	}
}