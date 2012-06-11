package org.trinity.display.x11.api.extension.render;

//currently unused
//TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.1
 */
public final class XRenderConstants {
	public static final int CP_REPEAT = (1 << 0);
	public static final int CP_ALPHA_MAP = (1 << 1);
	public static final int CP_ALPHA_X_ORIGIN = (1 << 2);
	public static final int CP_ALPHA_Y_ORIGIN = (1 << 3);
	public static final int CP_CLIP_X_ORIGIN = (1 << 4);
	public static final int CP_CLIP_Y_ORIGIN = (1 << 5);
	public static final int CP_CLIP_MASK = (1 << 6);
	public static final int CP_GRAPHICS_EXPOSURE = (1 << 7);
	public static final int CP_SUBWINDOW_MODE = (1 << 8);
	public static final int CP_POLY_EDGE = (1 << 9);
	public static final int CP_POLY_MODE = (1 << 10);
	public static final int CP_DITHER = (1 << 11);
	public static final int CP_COMPONENT_ALPHA = (1 << 12);
	public static final int CP_LAST_BIT = 11;

	public static final int PICT_FORMAT_ID = (1 << 0);
	public static final int PICT_FORMAT_TYPE = (1 << 1);
	public static final int PICT_FORMAT_DEPTH = (1 << 2);
	public static final int PICT_FORMAT_RED = (1 << 3);
	public static final int PICT_FORMAT_RED_MASK = (1 << 4);
	public static final int PICT_FORMAT_GREEN = (1 << 5);
	public static final int PICT_FORMAT_GREEN_MASK = (1 << 6);
	public static final int PICT_FORMAT_BLUE = (1 << 7);
	public static final int PICT_FORMAT_BLUE_MASK = (1 << 8);
	public static final int PICT_FORMAT_ALPHA = (1 << 9);
	public static final int PICT_FORMAT_ALPHA_MASK = (1 << 10);
	public static final int PICT_FORMAT_COLORMAP = (1 << 11);

	/**
	 * An <code>XRenderConstants</code> can not be instantiated. Calling this
	 * will result in an <code>InstantiationError</code>.
	 */
	private XRenderConstants() {
		throw new InstantiationError("This class can not be instaniated.\n"
				+ "Instead, directly use the provided static methods.");
	}
}
