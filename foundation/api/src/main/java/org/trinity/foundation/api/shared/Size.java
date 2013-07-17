package org.trinity.foundation.api.shared;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Size {

	private final int width, height;

	public Size(@Nonnegative final int width,
                @Nonnegative final int height) {
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