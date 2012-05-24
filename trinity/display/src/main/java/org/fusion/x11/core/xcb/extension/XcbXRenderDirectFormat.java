/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.xcb.extension;

import org.fusion.x11.core.extension.XRenderDirectFormat;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public final class XcbXRenderDirectFormat implements XRenderDirectFormat {

	private final int red;
	private final int redMask;
	private final int green;
	private final int greenMask;
	private final int blue;
	private final int blueMask;
	private final int alpha;
	private final int alphaMask;

	public XcbXRenderDirectFormat(final int red,
	                              final int redMask,
	                              final int green,
	                              final int greenMask,
	                              final int blue,
	                              final int blueMask,
	                              final int alpha,
	                              final int alphaMask) {
		this.red = red;
		this.redMask = redMask;
		this.green = green;
		this.greenMask = greenMask;
		this.blue = blue;
		this.blueMask = blueMask;
		this.alpha = alpha;
		this.alphaMask = alphaMask;
	}

	@Override
	public int getRed() {
		return this.red;
	}

	@Override
	public int getRedMask() {
		return this.redMask;
	}

	@Override
	public int getGreen() {
		return this.green;
	}

	@Override
	public int getGreenMask() {
		return this.greenMask;
	}

	@Override
	public int getBlue() {
		return this.blue;
	}

	@Override
	public int getBlueMask() {
		return this.blueMask;
	}

	@Override
	public int getAlpha() {
		return this.alpha;
	}

	@Override
	public int getAlphaMask() {
		return this.alphaMask;
	}

}
