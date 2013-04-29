package org.trinity.shellplugin.wm.api;

import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.shared.Size;

@Immutable
public class ImageDefinition {

	private final byte[] imageData;
	private final Size imageSize;
	private final String colorModel;

	public ImageDefinition(final byte[] imageData, final Size imageSize,
			final String colorModel) {
		this.imageData = imageData;
		this.imageSize = imageSize;
		this.colorModel = colorModel;
	}

	public byte[] getImageData() {
		// immutable!
		final byte[] imageDataCopy = new byte[this.imageData.length];
		System.arraycopy(this.imageData, 0, imageDataCopy, 0,
				this.imageData.length);
		return imageDataCopy;
	}

	public Size getImageSize() {
		return this.imageSize;
	}

	public String getColorModel() {
		return this.colorModel;
	}
}