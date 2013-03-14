package org.trinity.foundation.api.shared;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ImmutableSize implements Size {

	private final int width;
	private final int height;

	public ImmutableSize(	final int width,
							final int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {

		return this.height;
	}
}