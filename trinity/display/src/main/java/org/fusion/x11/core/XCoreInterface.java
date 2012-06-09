// /*
// * This file is part of Fusion-X11. Fusion-X11 is free software: you can
// * redistribute it and/or modify it under the terms of the GNU General Public
// * License as published by the Free Software Foundation, either version 3 of
// the
// * License, or (at your option) any later version. Fusion-X11 is distributed
// in
// * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
// * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// See
// * the GNU General Public License for more details. You should have received a
// * copy of the GNU General Public License along with Fusion-X11. If not, see
// * <http://www.gnu.org/licenses/>.
// */
// package org.fusion.x11.core;
//
// import org.fusion.display.x11.api.extension.XExtensionFactory;
// import org.fusion.x11.core.input.XKeySymbol;
// import org.fusion.x11.core.input.XKeyboard;
// import org.fusion.x11.core.input.XMouse;
// import org.trinity.core.display.api.DisplayEventSelector;
// import org.trinity.core.display.api.event.ClientMessageEvent;
// import org.trinity.core.display.api.event.DisplayEvent;
// import org.trinity.core.geometry.api.Coordinates;
// import org.trinity.core.input.api.Button;
// import org.trinity.core.input.api.InputModifiers;
// import org.trinity.core.input.api.Key;
// import org.trinity.display.x11.impl.XDisplayImpl;
// import org.trinity.display.x11.impl.XWindowImpl;
//
// // TODO documentation
// /**
// * The <code>XCoreInterface</code> defines all the calls that can be made to
// the
// * X display server by the fusion-x11 library.
// * <p>
// * Each method has a short description of the equivalent Xlib call. This does
// * not guarantee that the actual X related call is made in the underlying
// * implementation but indicates what behavior should be expected from the X
// * display server.
// *
// * @author Erik De Rijcke
// * @since 1.0
// */
// public interface XCoreInterface {
//
// // /**
// // * Send a client message to the given <code>XWindow</code> asking it to
// // * terminate gracefully. This call does not guarantee that the
// // * <code>XWindow</code> will be terminated.
// // *
// // * @param window
// // */
// // void requestDestroyWindow(XWindow window);
//
// /**
// * Destroy the native X11 window, represented by the given
// * <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XDestroyWindow() function destroys the
// * specified window as well as all of its subwindows and causes the X server
// * to generate a DestroyNotify event for each window. The window should
// * never be referenced again. </i>
// *
// * @param window
// * An {@link XWindowImpl}.
// */
// void forceDestroyWindow(XWindowImpl window);
//
// /**
// * Enable the propagation of <code>DisplayEvent</code>s from the native X11
// * window, represented by the given <code>XWindow</code>. The given
// * <code>EventPropagation</code> indicates which <code>DisplayEvent</code>
// * should be propagated.
// * <p>
// * From the Xlib manual:</br><i>The XSelectInput() function requests that
// * the X server report the events associated with the specified event mask.
// * Initially, X will not report any of these events. Events are reported
// * relative to a window.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @param eventMasks
// * An {@link EventPropagator}.
// */
// void enableEvents(XWindowImpl window, DisplayEventSelector... eventMasks);
//
// /**
// * Flush all pending request to the native X11 display represented by the
// * given <code>XDisplay</code>.
// * <p>
// * From the Xlib manual:</br><i>The XFlush() function flushes the output
// * buffer. Most client applications need not use this function because the
// * output buffer is automatically flushed as needed.</i>
// *
// * @param display
// * An {@link XDisplayImpl}.
// */
// void flush(XDisplayImpl display);
//
// /**
// * Give focus to the native X11 window, represented by the given
// * <code>XWindow</code> .
// * <p>
// * From the Xlib manual:</br>The XSetInputFocus() function changes the input
// * focus and the last-focus-change time<i></i>
// *
// * @param window
// * An {@link XWindowImpl}.
// */
// void focusWindow(XWindowImpl window);
//
// /**
// * All native X11 windows, represented by an array of <code>XWindow</code>s,
// * that have the given native X11 window, represented by the given
// * <code>XWindow</code>, directly or indirectly as it's parent.
// * <p>
// * From the Xlib manual:</br>The children are listed in current stacking
// * order, from bottommost (first) to topmost (last).<i></i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @return An array of native X11 windows, represented by an array of
// * <code>XWindow</code>s.
// */
// XWindowImpl[] getChildWindows(XWindowImpl window);
//
// /**
// * The next native X11 event, represented by an <code>DisplayEvent</code>
// * from the given native X11 display, represented by the given
// * <code>XDisplay</code>.
// * <p>
// * From the Xlib manual:</br><i>The XNextEvent() function copies the first
// * event from the event queue into the specified XEvent structure and then
// * removes it from the queue. If the event queue is empty, XNextEvent()
// * flushes the output buffer and blocks until an event is received.</i>
// *
// * @param display
// * An {@link XDisplayImpl}.
// * @return An {@link DisplayEvent}.
// */
// DisplayEvent getNextEvent(XDisplayImpl display);
//
// /**
// * The native X11 root window, represented by an <code>XWindow</code>, from
// * the given native X11 display, represented by the given
// * <code>XDisplay</code>.
// * <p>
// * From the Xlib manual:</br><i>The root window for the default screen.</i>
// *
// * @param display
// * An {@link XDisplayImpl}.
// * @return An {@link XWindowImpl}.
// */
// XWindowImpl getRootWindow(XDisplayImpl display);
//
// /**
// * The native X11 window attributes, represented by an
// * <code>XWindowAttributes</code>, from the given native X11 window,
// * represented by the given <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XGetWindowAttributes() function returns
// * the current attributes for the specified window to an XWindowAttributes
// * structure</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @return An {@link XWindowAttributes}.
// */
// XWindowAttributes getWindowAttributesCopy(XWindowImpl window);
//
// /**
// * The native X11 window geometry, represented by an
// * <code>XWindowGeometry</code>, from the given native X11 window,
// * represented by the given <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XGetGeometry() the current geometry of
// * the drawable. The geometry of the drawable includes the x and y
// * coordinates, width and height.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @return An {@link XWindowGeometry}.
// */
// XWindowGeometry getWindowGeometry(XWindowImpl window);
//
// /**
// * Lower the given native X11 window, represented by the given
// * <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XLowerWindow() function lowers the
// * specified window to the bottom of the stack so that it does not obscure
// * any sibling windows. If the windows are regarded as overlapping sheets of
// * paper stacked on a desk, then lowering a window is analogous to moving
// * the sheet to the bottom of the stack but leaving its x and y location on
// * the desk constant.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// */
// void lowerWindow(XWindowImpl window);
//
// /**
// * Map the given native X11 window, represented by the given
// * <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XMapWindow() function maps the window
// * and all of its subwindows that have had map requests. Mapping a window
// * that has an unmapped ancestor does not display the window but marks it as
// * eligible for display when the ancestor becomes mapped. Such a window is
// * called unviewable. When all its ancestors are mapped, the window becomes
// * viewable and will be visible on the screen if it is not obscured by
// * another window. This function has no effect if the window is already
// * mapped.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// */
// void mapWindow(XWindowImpl window);
//
// /**
// * Move and resize the given native X11 window, represented by the given
// * <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XMoveResizeWindow() function changes the
// * size and location of the specified window without raising it.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @param x
// * the new relative X coordinate in pixels for the given
// * <code>XWindow</code>
// * @param y
// * the new relative Y coordinate in pixels for the given
// * <code>XWindow</code>
// * @param width
// * the new width in pixels for the given <code>XWindow</code>
// * @param height
// * the new height in pixels for the given <code>XWindow</code>
// */
// void moveResizeWindow(XWindowImpl window, int x, int y, int width, int
// height);
//
// /**
// * Move the given native X11 window, represented by the given
// * <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XMoveWindow() function moves the
// * specified window to the specified x and y coordinates, but it does not
// * change the window's size, raise the window, or change the mapping state
// * of the window.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @param x
// * the new relative X coordinate in pixels for the given
// * <code>XWindow</code>
// * @param y
// * the new relative Y coor
// */
// void moveWindow(XWindowImpl window, int x, int y);
//
// /**
// * Open a connection to an X11 display. After a connection is made, a native
// * X11 display is returned, represented by an <code>XDisplay</code>.
// * <p>
// * From the Xlib manual:</br><i>The XOpenDisplay() function returns a
// * Display structure that serves as the connection to the X server and that
// * contains all the information about that X server. XOpenDisplay() connects
// * your application to the X server through TCP or DECnet communications
// * protocols, or through some local inter-process communication protocol. If
// * the hostname is a host machine name and a single colon (:) separates the
// * hostname and display number, XOpenDisplay() connects using TCP streams.
// * If the hostname is not specified, Xlib uses whatever it believes is the
// * fastest transport. If the hostname is a host machine name and a double
// * colon (::) separates the hostname and display number, XOpenDisplay()
// * connects using DECnet. A single X server can support any or all of these
// * transport mechanisms simultaneously. A particular Xlib implementation can
// * support many more of these transport mechanisms</i>
// *
// * @param displayPlatform
// * The {@link XDisplayPlatform} on which behalf the native X11
// * display connection was made.
// * @param display
// * The display name of the native X11 display.
// * @param screenNr
// * The screen number of the native X11 display.
// * @return an {@link XDisplayImpl}.
// */
// XDisplayImpl openDisplay( XDisplayPlatform displayPlatform,
// String display,
// int screenNr);
//
// /**
// * Make the given native X11 window, represented by the given
// * <code>XWindow</code>, ignore the substructure redirect event mask that
// * could be set on it's parent.
// * <p>
// * From the Xlib manual:</br><i>To control window placement or to add
// * decoration, a window manager often needs to intercept (redirect) any map
// * or configure request. Pop-up windows, however, often need to be mapped
// * without a window manager getting in the way. To control whether an
// * InputOutput or InputOnly window is to ignore these structure control
// * facilities, use the override-redirect flag. The override-redirect flag
// * specifies whether map and configure requests on this window should
// * override a SubstructureRedirectMask on the parent.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @param override
// * True if override should be enabled, false if not.
// */
// void overrideRedirectWindow(XWindowImpl window, boolean override);
//
// /**
// * Raise the given native X11 window, represented by the given
// * <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>The XRaiseWindow() function raises the
// * specified window to the top of the stack so that no sibling window
// * obscures it. If the windows are regarded as overlapping sheets of paper
// * stacked on a desk, then raising a window is analogous to moving the sheet
// * to the top of the stack but leaving its x and y location on the desk
// * constant.</i>
// *
// * @param window
// * an {@link XWindowImpl}.
// */
// void raiseWindow(XWindowImpl window);
//
// /**
// * Reparent the given native X11 window, represented by the given
// * <code>XWindow</code>, with the given coordinates to the given parent
// * native X11 window, represented by the given <code>XWindow</code>.
// * <p>
// * From the Xlib manual:</br><i>If the specified window is mapped,
// * XReparentWindow() automatically performs an UnmapWindow request on it,
// * removes it from its current position in the hierarchy, and inserts it as
// * the child of the specified parent. The window is placed in the stacking
// * order on top with respect to sibling windows.</i>
// *
// * @param parent
// * A parent {@link XWindowImpl}.
// * @param child
// * A child {@link XWindowImpl}.
// * @param newX
// * The new child X coordinate in pixels.
// * @param newY
// * The new child Y coordinate in pixels.
// */
// void reparentWindow(XWindowImpl parent, XWindowImpl child, int newX, int
// newY);
//
// /**
// * Resize the native X11 window, represented by the given
// * <code>XWindow</code>
// * <p>
// * From the Xlib manual:</br><i>he XResizeWindow() function changes the
// * inside dimensions of the specified window, not including its borders.
// * This function does not change the window's upper-left coordinate or the
// * origin and does not restack the window.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// * @param width
// * The new width in pixels.
// * @param height
// * The new height in pixels.
// */
// void resizeWindow(XWindowImpl window, int width, int height);
//
// /**
// * Close the X native display connection, represented by the given
// * <code>XDisplay</code>.
// * <p>
// * From the Xlib manual:</br><i>The XCloseDisplay() function closes the
// * connection to the X server for the display specified in the Display
// * structure and destroys all windows, resource IDs, or other resources that
// * the client has created on this display.</i>
// *
// * @param display
// * An {@link XDisplayImpl}.
// */
// void shutDownDisplay(XDisplayImpl display);
//
// /**
// * Make the native X11 window, represented by the given <code>XWindow</code>
// * , invisible.
// * <p>
// * From the Xlib manual:</br><i>The XUnmapWindow() function unmaps the
// * specified window and causes the X server to generate an UnmapNotify
// * event. If the specified window is already unmapped, XUnmapWindow() has no
// * effect. Normal exposure processing on formerly obscured windows is
// * performed. Any child window will no longer be visible until another map
// * call is made on the parent. In other words, the subwindows are still
// * mapped but are not visible until the parent is mapped.</i>
// *
// * @param window
// * An {@link XWindowImpl}.
// */
// void unmapWindow(XWindowImpl window);
//
// /**
// * Update the given <code>XMouse</code>'s values.
// * <p>
// * From the Xlib manual:</br><i>The XQueryPointer() function returns the
// * root window the pointer is logically on and the pointer coordinates
// * relative to the root window's origin.</i>
// *
// * @param mousePointer
// * An {@link XMouse}.
// */
// void updateXMousePointer(XMouse mousePointer);
//
// /**
// * @param display
// * @return
// */
// XKeyboard initXKeyboard(XDisplayImpl display);
//
// /**
// * @param display
// * @return
// */
// XMouse initXMouse(XDisplayImpl display);
//
// /**
// * @param keySymsPeer
// * @param keyCode
// * @param keyColumn
// * @return
// */
// long getXKeySymbol(long keySymsPeer, Key key, int keyColumn);
//
// /**
// * @param keySymsPeer
// * @param keySymbolCode
// * @return
// */
// Key[] getXKeyCodes(long keySymsPeer, XKeySymbol keySymbolCode);
//
// /**
// * @param key
// * @param inputModifiers
// */
// void grabKey(XWindowImpl window, Key key, InputModifiers inputModifiers);
//
// /**
// * @param window
// * @param button
// * @param inputModifiers
// */
// void grabButton(XWindowImpl window, Button button, InputModifiers
// inputModifiers);
//
// /**
// * @param window
// * @param key
// * @param inputModifiers
// */
// void ungrabKey(XWindowImpl window, Key key, InputModifiers inputModifiers);
//
// /**
// * @param window
// * @param button
// * @param inputModifiers
// */
// void ungrabButton( XWindowImpl window,
// Button button,
// InputModifiers inputModifiers);
//
// /**
// * @param display
// * @param atomName
// * @return
// */
// int internAtom(XDisplayImpl display, String atomName);
//
// /**
// * @param window
// * @param property
// * @param propertyInstanceType
// * @param propertyValueContainer
// */
// void setPropertyInstance( XWindowImpl window,
// XAtom property,
// XAtom propertyInstanceType,
// DataContainer<?> propertyValueContainer);
//
// /**
// * @param window
// * @param property
// * @param propertyValueContainer
// */
// FlexDataContainer getPropertyValue(XWindowImpl window, XAtom property);
//
// /**
// * @param window
// * @param clientMessageEvent
// */
// void sendClientMessage( final XWindowImpl window,
// final ClientMessageEvent clientMessageEvent);
//
// /**
// * @param window
// */
// void grabKeyboard(XWindowImpl window);
//
// /**
// * @param window
// */
// void ungrabKeybard(XWindowImpl window);
//
// /**
// * @param window
// */
// void grabMouse(XWindowImpl window);
//
// /**
// * @param window
// */
// void ungrabMouse(XWindowImpl window);
//
// /**
// * @param display
// * @return
// */
// XWindowImpl getInputFocus(XDisplayImpl display);
//
// /**
// * @param display
// * @return
// */
// XExtensionFactory initXExtensions(XDisplayImpl display);
//
// /**
// * @param display
// * @return
// */
// XWindowImpl createNewWindow(XDisplayImpl display);
//
// /**
// * @param selectionAtom
// * @param owner
// */
// void setSelectionOwner(XAtom selectionAtom, XWindowImpl owner);
//
// /**
// * @param selectionAtom
// * @return
// */
// XWindowImpl getSelectionOwner(XAtom selectionAtom);
//
// /**
// * @param xWindow
// */
// void addToSaveSet(XWindowImpl xWindow);
//
// /**
// * @param xWindow
// */
// void removeFromSaveSet(XWindowImpl xWindow);
//
// /**
// * @param xWindow
// * @param source
// * @param sourceX
// * @param sourceY
// * @return
// */
// Coordinates translateCoordinates( XWindowImpl xWindow,
// XWindowImpl source,
// int sourceX,
// int sourceY);
//
// }
