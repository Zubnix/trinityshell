/*
 * /* This file is part of Fusion-X11.
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
package org.fusion.x11.nativeHelpers;

import java.io.IOException;

import com.wapmx.nativeutils.jniloader.NativeLoader;

/**
 * A <code>FusionNativeLibLoader</code> loads the native library that is need to
 * talk with the X display server. This library has to be located on the
 * classpath.
 * <p>
 * A call to {@link FusionNativeLibLoader#loadNativeFusionXcb()} loads the
 * library implemented with Xcb.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class FusionNativeLibLoader {
	private static final String ARCH = System.getProperty("os.arch");

	// TODO determine native lib name from property
	public static final String LIBNAME_FUSION_XCB = String.format(
			"fusionxcb_%s", FusionNativeLibLoader.ARCH);

	/**
	 * 
	 * @throws IOException
	 */
	public static void loadNativeFusionXcb() throws IOException {
		if (FusionNativeLibLoader.ARCH.equals("i386")
				|| FusionNativeLibLoader.ARCH.equals("amd64")) {
			FusionNativeLibLoader
					.loadNativeLib(FusionNativeLibLoader.LIBNAME_FUSION_XCB);
		} else {
			throw new UnsupportedOperationException(String.format(
					"Arch: %s not supported", FusionNativeLibLoader.ARCH));
		}
	}

	/**
	 * Load the native library, identified by the given library name on the
	 * classpath.
	 * 
	 * @param libName
	 *            the relative path of the library on the classpath.
	 * @throws IOException
	 *             Thrown when the library is not found or could not be read.
	 */
	private static void loadNativeLib(final String libName) throws IOException {
		NativeLoader.loadLibrary(libName);

	}

	/**
	 * A <code>FusionNativeLibLoader</code> can not be instantiated. Calling
	 * this constructor will throw an <code>InstantiationError</code>.
	 */
	private FusionNativeLibLoader() {
		throw new InstantiationError();
	}
}
