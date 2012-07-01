/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.xcb.jni;

import java.nio.ByteBuffer;

// TODO documentation
/**
 * The <code>XCoreNative</code> class statically groups a number of jni calls
 * for a native library. This native library can implement these jni native
 * methods to talk to the X display server.
 * <p>
 * The class <code>XCoreNative</code> can not be instantiated. Attempts to
 * instantiate this class will result in an <code>InstantiationError</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class Xcb4J {

	public static native boolean
			nativeTranslateCoordinates(	long displayAddress,
										long windowId,
										long sourceWindowId,
										int sourceX,
										int sourceY,
										ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGetInputFocus(	long displayAddress,
														ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param time
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeUngrabKeyboard(	long displayAddress,
														int time,
														ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param time
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeUngrabMouse(	long displayAddress,
													int time,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowId
	 * @param time
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGrabMouse(	long displayAddress,
													long windowId,
													int time,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowId
	 * @param time
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGrabKeyboard(long displayAddress,
													long windowId,
													int time,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param inputDetail
	 * @param modifiers
	 * @param blocking
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGrabKey(	long displayAddress,
												long windowID,
												long inputDetail,
												long modifiers,
												boolean blocking,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param inputDetail
	 * @param modifiers
	 * @param blocking
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGrabButton(	long displayAddress,
													long windowID,
													long inputDetail,
													long modifiers,
													boolean blocking,
													ByteBuffer buffer);

	/**
	 * Call the native code that will destroy the native window identified by
	 * the given window id.
	 * <p>
	 * This method will return <code>true</code> if a native error occurs. The
	 * error can be read from the given <code>ByteBuffer</code>.
	 * <p>
	 * If no error occurs this method will return <code>false</code>.
	 * <p>
	 * How returned information is written to the given <code>ByteBuffer</code>
	 * depends on the native code implementation.
	 * 
	 * @param displayAddress
	 *            The pointer of a display struct that holds a connection to the
	 *            X server
	 * @param windowID
	 *            The window id of the native X window.
	 * @param buffer
	 *            A {@link ByteBuffer} for communication with the native code.
	 * @return True if an error occurs, false if not.
	 */
	public static native boolean Destroy(	long displayAddress,
											long windowID,
											ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeFlush(	long displayAddress,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param currentWindowGeometryResult
	 * @return
	 */
	public static native
			boolean
			nativeGetCurrentWindowGeometry(	long displayAddress,
											long windowID,
											ByteBuffer currentWindowGeometryResult);

	/**
	 * 
	 * @param displayAddress
	 * @param nextEventResult
	 * @return
	 */
	public static native boolean nativeGetNextEvent(long displayAddress,
													ByteBuffer nextEventResult);

	/**
	 * 
	 * @param displayAddress
	 * @param pointerInfo
	 * @return
	 */
	public static native boolean nativeGetPointerInfo(	long displayAddress,
														ByteBuffer pointerInfo);

	/**
	 * 
	 * @param displayAddress
	 * @param pointerInfo
	 * @return
	 */
	public static native boolean nativeGetRootWindow(	long displayAddress,
														ByteBuffer pointerInfo);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param windowAttributesResult
	 * @return
	 */
	public static native boolean
			nativeGetWindowAttributes(	long displayAddress,
										long windowID,
										ByteBuffer windowAttributesResult);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param time
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGiveFocus(	long displayAddress,
													long windowID,
													int time,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeLower(	long displayAddress,
												long windowID,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeMap(	long displayAddress,
											long windowID,
											ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param x
	 * @param y
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeMove(long displayAddress,
											long windowID,
											int x,
											int y,
											ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeMoveResize(	long displayAddress,
													long windowID,
													int x,
													int y,
													int width,
													int height,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayName
	 * @param pointerInfo
	 * @return
	 */
	public static native boolean nativeOpenDisplay(	String displayName,
													ByteBuffer pointerInfo);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param eventMask
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeSelectEvent(	long displayAddress,
													long windowID,
													long eventMask,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeRaise(	long displayAddress,
												long windowID,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param childWindowID
	 * @param parentWindowID
	 * @param x
	 * @param y
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeReparent(long displayAddress,
												long childWindowID,
												long parentWindowID,
												int x,
												int y,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param width
	 * @param height
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeResize(	long displayAddress,
												long windowID,
												int width,
												int height,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param save
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeSaveYourself(long displayAddress,
													long windowID,
													boolean save,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeShutDown(long displayAddress,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowID
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeUnmap(	long displayAddress,
												long windowID,
												ByteBuffer buffer);

	/**
	 * 
	 * @param keysymsPeer
	 * @param keyCode
	 * @param keyColumn
	 * @return
	 */
	public static native boolean nativeGetKeySymbol(long keysymsPeer,
													int keyCode,
													int keyColumn,
													ByteBuffer buffer);

	/**
	 * 
	 * @param keysymsPeer
	 * @param keysymbolCode
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGetKeyCodes(	long keysymsPeer,
													long keysymbolCode,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddres
	 * @param windowId
	 * @param keyCode
	 * @param modifiersMask
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGrabKey(	long displayAddres,
												long windowId,
												int keyCode,
												int modifiersMask,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddres
	 * @param windowId
	 * @param buttonCode
	 * @param modifiersMask
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeGrabButton(	long displayAddres,
													long windowId,
													int buttonCode,
													int modifiersMask,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddres
	 * @param windowId
	 * @param keyCode
	 * @param modifiersMask
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeUngrabKey(	long displayAddres,
													long windowId,
													int keyCode,
													int modifiersMask,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddres
	 * @param windowId
	 * @param buttonCode
	 * @param modifiersMask
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeUngrabButton(long displayAddres,
													long windowId,
													int buttonCode,
													int modifiersMask,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowId
	 * @param propertyAtomId
	 * @param typeAtomId
	 * @param format
	 * @param data
	 * @param result
	 * @return
	 */
	public static native boolean nativeChangeProperty(	long displayAddress,
														long windowId,
														long propertyAtomId,
														long typeAtomId,
														int format,
														byte[] data,
														ByteBuffer result);

	/**
	 * 
	 * @param displayAddress
	 * @param windowId
	 * @param propertyAtomId
	 * @param result
	 * @return
	 */
	public static native boolean nativeGetProperty(	long displayAddress,
													long windowId,
													long propertyAtomId,
													ByteBuffer result);

	/**
	 * 
	 * @param displayAddress
	 * @param windowId
	 * @param atomId
	 * @param format
	 * @param data
	 * @param result
	 * @return
	 */
	public static native boolean
			nativeSendClientMessage(long displayAddress,
									long windowId,
									long atomId,
									int format,
									byte[] data,
									ByteBuffer result);

	/**
	 * 
	 * @param displayAddress
	 * @param string
	 * @param buffer
	 * @return
	 */
	public static native boolean nativeRegisterAtom(final long displayAddress,
													String string,
													ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param selectionAtomId
	 * @param ownerWindowId
	 * @param time
	 * @param buffer
	 * @return
	 */
	public static native boolean
			nativeSetSelectionOwner(long displayAddress,
									long selectionAtomId,
									long ownerWindowId,
									int time,
									ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param selectionAtomId
	 * @param buffer
	 * @return
	 */
	public static native boolean
			nativeGetSelectionOwner(long displayAddress,
									long selectionAtomId,
									ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @return
	 */
	public static native boolean nativeCreateNewWindow(	long displayAddress,
														ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowId
	 * @return
	 */
	public static native boolean AddToSaveSet(	long displayAddress,
												long windowId,
												ByteBuffer buffer);

	/**
	 * 
	 * @param displayAddress
	 * @param windowId
	 * @return
	 */
	public static native boolean
			nativeRemoveFromSaveSet(long displayAddress,
									long windowId,
									ByteBuffer buffer);

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
	 * An <code>XCoreNative</code> can not be instantiated. Calling this will
	 * result in an <code>InstantiationError</code>.
	 */
	private Xcb4J() {
		throw new InstantiationError("This class can not be instaniated.\n"
				+ "Instead, directly use the provided static methods.");
	}
}