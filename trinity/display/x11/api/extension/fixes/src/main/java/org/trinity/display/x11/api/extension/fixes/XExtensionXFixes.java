package org.trinity.display.x11.api.extension.fixes;

import org.trinity.display.x11.api.core.XExtension;
import org.trinity.display.x11.api.core.XWindow;
import org.trinity.display.x11.api.extension.render.XRenderPicture;

// currently unused
// TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XExtensionXFixes extends XExtension {
	XFixesXServerRegion createRegionFromWindow(	final XWindow window,
												int regionBounding);

	void translateRegion(XFixesXServerRegion region, int x, int y);

	void setPictureClipRegion(	XRenderPicture picture,
								int clipXOrigin,
								int clipYOrigin,
								XFixesXServerRegion region);

	void destroyRegion(XFixesXServerRegion region);
}
