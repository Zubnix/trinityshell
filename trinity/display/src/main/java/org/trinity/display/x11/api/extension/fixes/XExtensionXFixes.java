package org.trinity.display.x11.api.extension.fixes;

import org.trinity.display.x11.api.extension.XExtension;
import org.trinity.display.x11.api.extension.render.XRenderPicture;
import org.trinity.display.x11.impl.XWindowImpl;

//currently unused
//TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XExtensionXFixes extends XExtension {
	XFixesXServerRegion createRegionFromWindow(final XWindowImpl window,
			int regionBounding);

	void translateRegion(XFixesXServerRegion region, int x, int y);

	void setPictureClipRegion(XRenderPicture picture, int clipXOrigin,
			int clipYOrigin, XFixesXServerRegion region);

	void destroyRegion(XFixesXServerRegion region);
}
