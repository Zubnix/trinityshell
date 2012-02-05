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
package org.fusion.x11.core;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class XProtocolConstants {
	// various foo
	public static final long CURRENT_TIME = 0;

	public static final int IS_UNMAPPED = 0;
	public static final int IS_UNVIEWABLE = 1;
	public static final int IS_VIEWABLE = 2;

	public static final long VISIBILITY_CHANGE_MASK = 1 << 16;

	public static final int PROPERTY_DELETE = 1;

	// event selection masks
	public static final long NO_EVENT_MASK = 0;
	public static final long KEY_PRESS_MASK = 1;
	public static final long KEY_RELEASE_MASK = 2;
	public static final long BUTTON_PRESS_MASK = 4;
	public static final long BUTTON_RELEASE_MASK = 8;
	public static final long ENTER_WINDOW_MASK = 16;
	public static final long LEAVE_WINDOW_MASK = 32;
	// (define x:Pointer-Motion-Mask 64)
	// (define x:Pointer-Motion-Hint-Mask 128)
	// (define x:Button1-Motion-Mask 256)
	// (define x:Button2-Motion-Mask 512)
	// (define x:Button3-Motion-Mask 1024)
	// (define x:Button4-Motion-Mask 2048)
	// (define x:Button5-Motion-Mask 4096)
	// (define x:Button-Motion-Mask 8192)
	// (define x:Keymap-State-Mask 16384)
	// (define x:Exposure-Mask 32768)
	// (define x:Visibility-Change-Mask 65536)
	public static final long STRUCTURE_NOTIFY_MASK = 131072;
	// (define x:Resize-Redirect-Mask 262144)
	public static final long SUBSTRUCTURE_NOTIFY_MASK = 524288;
	public static final long SUBSTRUCTURE_REDIRECT_MASK = 1048576;
	public static final long FOCUS_CHANGE_MASK = 2097152;
	public static final long PROPERTY_CHANGE_MASK = 4194304;
	// (define x:Colormap-Change-Mask 8388608)
	// (define x:Owner-Grab-Button-Mask 16777216)

	// event codes:
	public static final int KEY_PRESS = 2;
	public static final int KEY_RELEASE = 3;
	public static final int BUTTON_PRESS = 4;
	public static final int BUTTON_RELEASE = 5;
	public static final int MOTION_NOTIFY = 6;
	public static final int ENTER_NOTIFY = 7;
	public static final int LEAVE_NOTIFY = 8;
	public static final int FOCUS_IN = 9;
	public static final int FOCUS_OUT = 10;
	public static final int KEYMAP_NOTIFY = 11;
	public static final int EXPOSE = 12;
	public static final int GRAPHICS_EXPOSURE = 13;
	public static final int NO_EXPOSURE = 14;
	public static final int VISIBILITY_NOTIFY = 15;
	public static final int CREATE_NOTIFY = 16;
	public static final int DESTROY_NOTIFY = 17;
	public static final int UNMAP_NOTIFY = 18;
	public static final int MAP_NOTIFY = 19;
	public static final int MAP_REQUEST = 20;
	public static final int REPARENT_NOTIFY = 21;
	public static final int CONFIGURE_NOTIFY = 22;
	public static final int CONFIGURE_REQUEST = 23;
	public static final int GRAVITY_NOTIFY = 24;
	public static final int RESIZE_REQUEST = 25;
	public static final int CIRCULATE_NOTIFY = 26;
	public static final int CIRCULATE_REQUEST = 27;
	public static final int PROPERTY_NOTIFY = 28;
	public static final int SELECTION_CLEAR = 29;
	public static final int SELECTION_REQUEST = 30;
	public static final int SELECTION_NOTIFY = 31;
	public static final int COLORMAP_NOTIFY = 32;
	public static final int CLIENT_MESSAGE = 33;
	public static final int MAPPING_NOTIFY = 34;

	// window configure codes:
	public static final short CONFIG_WINDOW_X = 1;
	public static final short CONFIG_WINDOW_Y = 2;
	public static final short CONFIG_WINDOW_WIDTH = 4;
	public static final short CONFIG_WINDOW_HEIGHT = 8;

	// gravity codes
	public static final int GRAVITY_BIT_FORGET = 0;
	public static final int GRAVITY_WIN_UNMAP = 0;
	public static final int GRAVITY_NORTH_WEST = 1;
	public static final int GRAVITY_NORTH = 2;
	public static final int GRAVITY_NORTH_EAST = 3;
	public static final int GRAVITY_WEST = 4;
	public static final int GRAVITY_CENTER = 5;
	public static final int GRAVITY_EAST = 6;
	public static final int GRAVITY_SOUTH_WEST = 7;
	public static final int GRAVITY_SOUTH = 8;
	public static final int GRAVITY_SOUTH_EAST = 9;
	public static final int GRAVITY_STATIC = 10;

	// predefined atom ids:
	public static final int PRIMARY = 1;
	public static final int SECONDARY = 2;
	public static final int ARC = 3;
	public static final int ATOM = 4;
	public static final int BITMAP = 5;
	public static final int CARDINAL = 6;
	public static final int COLORMAP = 7;
	public static final int CURSOR = 8;
	public static final int CUT_BUFFER0 = 9;
	public static final int CUT_BUFFER1 = 10;
	public static final int CUT_BUFFER2 = 11;
	public static final int CUT_BUFFER3 = 12;
	public static final int CUT_BUFFER4 = 13;
	public static final int CUT_BUFFER5 = 14;
	public static final int CUT_BUFFER6 = 15;
	public static final int CUT_BUFFER7 = 16;
	public static final int DRAWABLE = 17;
	public static final int FONT = 18;
	public static final int INTEGER = 19;
	public static final int PIXMAP = 20;
	public static final int POINT = 21;
	public static final int RECTANGLE = 22;
	public static final int RESOURCE_MANAGER = 23;
	public static final int RGB_COLOR_MAP = 24;
	public static final int RGB_BEST_MAP = 25;
	public static final int RGB_BLUE_MAP = 26;
	public static final int RGB_DEFAULT_MAP = 27;
	public static final int RGB_GRAY_MAP = 28;
	public static final int RGB_GREEN_MAP = 29;
	public static final int RGB_RED_MAP = 30;
	public static final int STRING = 31;
	public static final int VISUALID = 32;
	public static final int WINDOW = 33;
	public static final int WM_COMMAND = 34;
	public static final int WM_HINTS = 35;
	public static final int WM_CLIENT_MACHINE = 36;
	public static final int WM_ICON_NAME = 37;
	public static final int WM_ICON_SIZE = 38;
	public static final int WM_NAME = 39;
	public static final int WM_NORMAL_HINTS = 40;
	public static final int WM_SIZE_HINTS = 41;
	public static final int WM_ZOOM_HINTS = 42;
	public static final int MIN_SPACE = 43;
	public static final int NORM_SPACE = 44;
	public static final int MAX_SPACE = 45;
	public static final int END_SPACE = 46;
	public static final int SUPERSCRIPT_X = 47;
	public static final int SUPERSCRIPT_Y = 48;
	public static final int SUBSCRIPT_X = 49;
	public static final int SUBSCRIPT_Y = 50;
	public static final int UNDERLINE_POSITION = 51;
	public static final int UNDERLINE_THICKNESS = 52;
	public static final int STRIKEOUT_ASCENT = 53;
	public static final int STRIKEOUT_DESCENT = 54;
	public static final int ITALIC_ANGLE = 55;
	public static final int X_HEIGHT = 56;
	public static final int QUAD_WIDTH = 57;
	public static final int WEIGHT = 58;
	public static final int POINT_SIZE = 59;
	public static final int RESOLUTION = 60;
	public static final int COPYRIGHT = 61;
	public static final int NOTICE = 62;
	public static final int FONT_NAME = 63;
	public static final int FAMILY_NAME = 64;
	public static final int FULL_NAME = 65;
	public static final int CAP_HEIGHT = 66;
	public static final int WM_CLASS = 67;
	public static final int WM_TRANSIENT_FOR = 68;

	// Error codes
	public static final int SUCCESS = 0;
	public static final int BAD_REQUEST = 1;
	public static final int BAD_VALUE = 2;
	public static final int BAD_WINDOW = 3;
	public static final int BAD_PIXMAP = 4;
	public static final int BAD_ATOM = 5;
	public static final int BAD_CURSOR = 6;
	public static final int BAD_FONT = 7;
	public static final int BAD_MATCH = 8;
	public static final int BAD_DRAWABLE = 9;
	public static final int BAD_ACCESS = 10;
	public static final int BAD_ALLOC = 11;
	public static final int BAD_COLOR = 12;
	public static final int BAD_GC = 13;
	public static final int BAD_ID_CHOICE = 14;
	public static final int BAD_NAME = 15;
	public static final int BAD_LENGTH = 16;
	public static final int BAD_IMPLEMENTATION = 17;
	public static final int BAD_FIRST_EXTENSION_ERROR = 18;
	public static final int BAD_LAST_EXTENSION_ERROR = 19;

	/**
	 * An <code>XProtocolConstants</code> can not be instantiated. Calling this
	 * will result in an <code>InstantiationError</code>.
	 */
	private XProtocolConstants() {
		throw new InstantiationError("This class can not be instaniated.\n"
				+ "Instead, directly use the provided static methods.");
	}
}
