package org.trinity.display.x11.api.extension.render;

import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XPixmap;

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public interface XRenderPictureAttributes {
	// Bool repeat;
	// Picture alpha_map;
	// int alpha_x_origin;
	// int alpha_y_origin;
	// int clip_x_origin;
	// int clip_y_origin;
	// Pixmap clip_mask;
	// Bool graphics_exposures;
	// int subwindow_mode;
	// int poly_edge;
	// int poly_mode;
	// Atom dither;
	// Bool component_alpha;

	boolean isRepeat();

	XRenderPicture getAlphaMap();

	int getAlphaXOrigin();

	int getAlphaYOrigin();

	int getClipXOrigin();

	int getClipYOrigin();

	XPixmap getClipMask();

	boolean isGraphicsExposures();

	XRenderSubwindowMode getSubwindowMode();

	int getPolyEdge();

	int getPolyMode();

	XAtom getDither();

	boolean isComponentAlpha();
}
