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

import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XPixmap;
import org.fusion.x11.core.extension.XRenderPicture;
import org.fusion.x11.core.extension.XRenderPictureAttributes;
import org.fusion.x11.core.extension.XRenderSubwindowMode;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbXRenderPictureAttributes implements XRenderPictureAttributes {

	private final boolean              repeat;
	private final XRenderPicture       alphaMap;
	private final int                  alphaXOrigin;
	private final int                  alphaYOrigin;
	private final int                  clipXOrigin;
	private final int                  clipYOrigin;
	private final XPixmap              clipMask;
	private final boolean              graphicsExposures;
	private final XRenderSubwindowMode subwindowMode;
	private final int                  polyEdge;
	private final int                  polyMode;
	private final XAtom                dither;
	private final boolean              componentAlpha;

	public XcbXRenderPictureAttributes(final boolean repeat,
	                                   final XRenderPicture alphaMap,
	                                   final int alphaXOrigin,
	                                   final int alphaYOrigin,
	                                   final int clipXOrigin,
	                                   final int clipYOrigin,
	                                   final XPixmap clipMask,
	                                   final boolean graphicsExposures,
	                                   final XRenderSubwindowMode subwindowMode,
	                                   final int polyEdge,
	                                   final int polyMode,
	                                   final XAtom dither,
	                                   final boolean componentAlpha) {
		this.repeat = repeat;
		this.alphaMap = alphaMap;
		this.alphaXOrigin = alphaXOrigin;
		this.alphaYOrigin = alphaYOrigin;
		this.clipXOrigin = clipXOrigin;
		this.clipYOrigin = clipYOrigin;
		this.clipMask = clipMask;
		this.graphicsExposures = graphicsExposures;
		this.subwindowMode = subwindowMode;
		this.polyEdge = polyEdge;
		this.polyMode = polyMode;
		this.dither = dither;
		this.componentAlpha = componentAlpha;
	}

	@Override
	public boolean isRepeat() {
		return this.repeat;
	}

	@Override
	public XRenderPicture getAlphaMap() {
		return this.alphaMap;
	}

	@Override
	public int getAlphaXOrigin() {
		return this.alphaXOrigin;
	}

	@Override
	public int getAlphaYOrigin() {
		return this.alphaYOrigin;
	}

	@Override
	public int getClipXOrigin() {
		return this.clipXOrigin;
	}

	@Override
	public int getClipYOrigin() {
		return this.clipYOrigin;
	}

	@Override
	public XPixmap getClipMask() {
		return this.clipMask;
	}

	@Override
	public boolean isGraphicsExposures() {
		return this.graphicsExposures;
	}

	@Override
	public XRenderSubwindowMode getSubwindowMode() {
		return this.subwindowMode;
	}

	@Override
	public int getPolyEdge() {
		return this.polyEdge;
	}

	@Override
	public int getPolyMode() {
		return this.polyMode;
	}

	@Override
	public XAtom getDither() {
		return this.dither;
	}

	@Override
	public boolean isComponentAlpha() {
		return this.componentAlpha;
	}

}
