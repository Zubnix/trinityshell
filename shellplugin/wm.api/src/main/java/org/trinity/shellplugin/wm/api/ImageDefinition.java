/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shellplugin.wm.api;

import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.shared.Size;

@Immutable
public class ImageDefinition {

	private final byte[] imageData;
	private final Size imageSize;
	private final String colorModel;

	public ImageDefinition(	final byte[] imageData,
							final Size imageSize,
							final String colorModel) {
		this.imageData = imageData;
		this.imageSize = imageSize;
		this.colorModel = colorModel;
	}

	public byte[] getImageData() {
		// immutable!
		final byte[] imageDataCopy = new byte[this.imageData.length];
		System.arraycopy(	this.imageData,
							0,
							imageDataCopy,
							0,
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
