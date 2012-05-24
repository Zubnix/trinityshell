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
package org.fusion.x11.core.xcb;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XcbCoreNative {

	/**
	 * 
	 * @param displayAddress
	 * @return
	 */
	public static native long nativeAllocateKeysyms(long displayAddress);

	/**
	 * 
	 * @param keysymsAddress
	 * @return
	 */
	public static native boolean nativeFreeKeysyms(long keysymsAddress);

	/**
	 * An <code>XcbCoreNative</code> can not be instantiated. Calling this will
	 * result in an <code>InstantiationError</code>.
	 */
	private XcbCoreNative() {
		throw new InstantiationError("This class can not be instaniated.\n"
				+ "Instead, directly use the provided static methods.");
	}
}