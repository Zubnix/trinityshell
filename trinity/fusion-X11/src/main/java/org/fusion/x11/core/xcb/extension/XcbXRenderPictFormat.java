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

import org.fusion.x11.core.XColormap;
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XResource;
import org.fusion.x11.core.extension.XRenderDirectFormat;
import org.fusion.x11.core.extension.XRenderPictFormat;
 

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public final class XcbXRenderPictFormat extends XResource implements
                XRenderPictFormat {

	private final int                 type;
	private final int                 depth;
	private final XRenderDirectFormat directFormat;
	private final XColormap           colormap;

	XcbXRenderPictFormat(final XID xid,
	                     final int type,
	                     final int depth,
	                     final XRenderDirectFormat directFormat,
	                     final XColormap colormap)    {
		super(xid);
		this.type = type;
		this.depth = depth;
		this.directFormat = directFormat;
		this.colormap = colormap;
	}

	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public int getDepth() {
		return this.depth;
	}

	@Override
	public XRenderDirectFormat getDirectFormat() {
		return this.directFormat;
	}

	@Override
	public XColormap getColormap() {
		return this.colormap;
	}
}
