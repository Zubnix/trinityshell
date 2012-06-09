package org.fusion.x11.core.xcb.extension;

import org.trinity.display.x11.api.extension.render.XRenderColor;

// currently unused
public class XcbXRenderColor implements XRenderColor {

	private final int red, green, blue, alpha;

	public XcbXRenderColor(final int red,
	                       final int green,
	                       final int blue,
	                       final int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	@Override
	public int getRed() {
		return this.red;
	}

	@Override
	public int getGreen() {
		return this.green;
	}

	@Override
	public int getBlue() {
		return this.blue;
	}

	@Override
	public int getAlpha() {
		return this.alpha;
	}
}
