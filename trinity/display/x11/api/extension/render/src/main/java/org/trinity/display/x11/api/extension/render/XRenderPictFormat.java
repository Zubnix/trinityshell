package org.trinity.display.x11.api.extension.render;

import org.trinity.display.x11.api.core.XColormap;
import org.trinity.foundation.display.api.DisplayResource;

// currently unused
// TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public interface XRenderPictFormat extends DisplayResource {

	int getType();

	int getDepth();

	XRenderDirectFormat getDirectFormat();

	XColormap getColormap();
}
