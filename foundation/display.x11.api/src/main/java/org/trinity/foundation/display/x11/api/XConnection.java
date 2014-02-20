/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.foundation.display.x11.api;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * A connection to an X display server.
 */
@NotThreadSafe
public interface XConnection {
	/**
	 * The XCB connection reference. The optional reference will be absent if no
	 * connection is present.
	 *
	 * @return The underlying native X connection.
	 */
	SWIGTYPE_p_xcb_connection_t getConnectionReference();

	/**
	 * Close the connection to the underlying X display server.
	 */
	void close();
}
