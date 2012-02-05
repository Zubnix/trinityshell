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
package org.fusion.x11.core.extension;

import java.nio.ByteBuffer;

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public final class XExtensionNative {

	public static native boolean nativeXDamageSubtract(long disiplayPeer,
	                                                   long damageId,
	                                                   long repairRegionId,
	                                                   long partsRegionId,
	                                                   ByteBuffer buffer);

	public static native boolean nativeXDamageCreate(long displayPeer,
	                                                 long windowId,
	                                                 int damageToReport,
	                                                 ByteBuffer buffer);

	public static native boolean nativeXDamageInit(long displayPeer,
	                                               int requiredMajorVersion,
	                                               int requiredMinorVersion,
	                                               ByteBuffer buffer);

	public static native boolean nativeXCompositeInit(long displayPeer,
	                                                  int requiredMajorVersion,
	                                                  int requiredMinorVersion,
	                                                  ByteBuffer buffer);

	public static native boolean nativeXCompositeRedirectSubwindow(long displayPeer,
	                                                               long windowId,
	                                                               int updateMode,
	                                                               ByteBuffer buffer);

	public static native boolean nativeXFixesInit(long displayPeer,
	                                              int requiredMajorVersion,
	                                              int requiredMinorVersion,
	                                              ByteBuffer buffer);

	// TODO native implementation
	public static native boolean nativeXFixesCreateRegionFromWindow(long displayPeer,
	                                                                long windowId,
	                                                                int regionBounding,
	                                                                ByteBuffer buffer);

	// TODO native implementation
	public static native boolean nativeXFixesTranslateRegion(long display,
	                                                         long regionId,
	                                                         int x,
	                                                         int y,
	                                                         ByteBuffer buffer);

	// TODO native implementation
	public static native boolean nativeXFixesSetPictureClipRegion(long display,
	                                                              long pictureId,
	                                                              int clipXOrigin,
	                                                              int clipYOrigin,
	                                                              long regionId,
	                                                              ByteBuffer buffer);

	// TODO native implementation
	public static native boolean nativeXFixesDestroyRegion(long display,
	                                                       long regionId,
	                                                       ByteBuffer buffer);

	public static native boolean nativeXRenderInit(long displayPeer,
	                                               int requiredMajorVersion,
	                                               int requiredMinorVersion,
	                                               ByteBuffer buffer);

	public static native boolean nativeXShapeInit(long displayPeer,
	                                              int requiredMajorVersion,
	                                              int requiredMinorVersion,
	                                              ByteBuffer buffer);

	public static native boolean nativeXSyncInit(long displayPeer,
	                                             int requiredMajorVersion,
	                                             int requiredMinorVersion,
	                                             ByteBuffer buffer);

	// TODO condition arguments
	public static native boolean nativeXSyncAwaitCondition(long displayPeer,
	                                                       ByteBuffer buffer);

	private XExtensionNative() {
		throw new Error("This class should not be instantiated. Instead use the provided static methods.");
	}

}
