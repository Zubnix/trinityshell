/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.x11.api;

import javax.annotation.concurrent.Immutable;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

@ExecutionContext(DisplayExecutor.class)
@Immutable
public class XcbErrorUtil {
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

	public static String toString(final xcb_generic_error_t generic_error_t) {
		return String.format(	"Native XCB: %s - error: %d - resource id: %d",
								XcbErrorUtil.getErrorString(generic_error_t.getError_code()),
								generic_error_t.getError_code(),
								generic_error_t.getResource_id());
	}

	public static String getErrorString(final short errorCode) {

		switch (errorCode) {
			case SUCCESS:
				return "success";

			case BAD_REQUEST:
				return "bad_request";

			case BAD_VALUE:
				return "bad_value";

			case BAD_WINDOW:
				return "bad_window";

			case BAD_PIXMAP:
				return "bad_pixmap";

			case BAD_ATOM:
				return "bad_atom";

			case BAD_CURSOR:
				return "bad_cursor";

			case BAD_FONT:
				return "bad_font";

			case BAD_MATCH:
				return "bad_match";

			case BAD_DRAWABLE:
				return "bad_drawable";

			case BAD_ACCESS:
				return "bad_access";

			case BAD_ALLOC:
				return "bad_alloc";

			case BAD_COLOR:
				return "bad_color";

			case BAD_GC:
				return "bad_gc";

			case BAD_ID_CHOICE:

				return "bad_id_choice";
			case BAD_NAME:

				return "bad_name";
			case BAD_LENGTH:

				return "bad_length";
			case BAD_IMPLEMENTATION:
				return "bad_implementation";

			case BAD_FIRST_EXTENSION_ERROR:

				return "bad_first_extension_error";
			case BAD_LAST_EXTENSION_ERROR:
				return "bad_last_extension_error";

			default:
				return "unknown error code";
		}
	}
}
