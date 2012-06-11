package org.trinity.display.x11.api.extension.render;

import org.fusion.x11.core.XColormap;
import org.trinity.core.display.api.DisplayResource;

//currently unused
//TODO documentation
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
