package org.trinity.foundation.api.shared;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Size {

	private final int width, height;

	public Size(final int width,
				final int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
}