package org.fusion.x11.core.extension;

import org.fusion.x11.core.XWindow;

//currently unused
//TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XExtensionXFixes extends XExtension {
	XFixesXServerRegion createRegionFromWindow(final XWindow window,
			int regionBounding);

	void translateRegion(XFixesXServerRegion region, int x, int y);

	void setPictureClipRegion(XRenderPicture picture, int clipXOrigin,
			int clipYOrigin, XFixesXServerRegion region);

	void destroyRegion(XFixesXServerRegion region);
}
