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
package org.trinity.display.x11.api.core.event;

import org.trinity.display.x11.api.core.XAtom;
import org.trinity.display.x11.api.core.XWindow;

import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 * 
 ****************************************/
public interface XEventFactory {
	XButtonEvent createXButtonEvent(@Assisted("eventCode") int eventCode,
									@Assisted("sequence") int sequence,
									@Assisted XWindow window,
									@Assisted XWindow root,
									@Assisted XWindow subwindow,
									@Assisted("time") int time,
									@Assisted("x") int x,
									@Assisted("y") int y,
									@Assisted("xRoot") int xRoot,
									@Assisted("yRoot") int yRoot,
									@Assisted("state") int state,
									@Assisted("button") int button,
									@Assisted boolean sameScreen);

	XCirculateEvent
			createXCirculateEvent(	@Assisted("eventCode") int eventCode,
									@Assisted("sequence") int sequence,
									@Assisted("event") XWindow event,
									@Assisted("window") XWindow window,
									@Assisted("place") int place);

	XClientMessageEvent
			createXClientMessageEvent(	@Assisted("eventCode") int eventCode,
										@Assisted("format") int format,
										@Assisted("sequence") int sequence,
										@Assisted XWindow window,
										@Assisted XAtom atom,
										@Assisted byte[] data);

	XColormapEvent createXColormapEvent();

			XConfigureEvent
			createXConfigureEvent(	@Assisted("eventCode") int eventCode,
									@Assisted("sequence") int sequence,
									@Assisted("event") XWindow event,
									@Assisted("window") XWindow window,
									@Assisted("aboveSibling") XWindow aboveSibling,
									@Assisted("x") int x,
									@Assisted("y") int y,
									@Assisted("width") int width,
									@Assisted("height") int height,
									@Assisted("borderWidth") int borderWidth,
									@Assisted boolean overrideRedirect);

			XConfigureRequestEvent
			createXConfigureRequestEvent(	@Assisted("eventCode") int eventCode,
											@Assisted("stackMode") int stackMode,
											@Assisted("sequence") int sequence,
											@Assisted("parent") XWindow parent,
											@Assisted("window") XWindow window,
											@Assisted("aboveSibling") XWindow aboveSibling,
											@Assisted("x") int x,
											@Assisted("y") int y,
											@Assisted("width") int width,
											@Assisted("height") int height,
											@Assisted("borderWidth") int borderWidth,
											@Assisted("valueMask") int valueMask);

	XCreateWindowEvent createXCreateWindowEvent();

	XCrossingEvent createXCrossingEvent();

	XDestroyWindowEvent
			createXDestroyWindowEvent(	@Assisted("eventCode") int eventCode,
										@Assisted("sequence") int sequence,
										@Assisted("event") XWindow event,
										@Assisted("window") XWindow window);

	XExposeEvent createXExposeEvent();

	XFocusEvent createXFocusEvent(	@Assisted("eventCode") int eventCode,
									@Assisted("detail") int detail,
									@Assisted("sequence") int sequence,
									@Assisted XWindow event,
									@Assisted("mode") int mode);

	XGraphicsExposeEvent createXGraphicsExposeEvent();

	XGravityEvent createXGravityEvent();

	XKeyEvent createXKeyEvent(	@Assisted("eventCode") int eventCode,
								@Assisted("detail") int detail,
								@Assisted("sequence") int sequence,
								@Assisted("root") XWindow root,
								@Assisted("event") XWindow event,
								@Assisted("child") XWindow child,
								@Assisted("time") long time,
								@Assisted("rootX") int rootX,
								@Assisted("rootY") int rootY,
								@Assisted("eventX") int eventX,
								@Assisted("eventY") int eventY,
								@Assisted("state") int state,
								@Assisted boolean sameScreen);

	XKeymapEvent createXKeymapEvent();

	XMapEvent createXMapEvent(	@Assisted("eventCode") int eventCode,
								@Assisted("sequence") int sequence,
								@Assisted("event") XWindow event,
								@Assisted("window") XWindow window,
								@Assisted boolean overrideRedirect);

	XMappingEvent createXMappingEvent();

	XMapRequestEvent
			createXMapRequestEvent(	@Assisted("eventCode") int eventCode,
									@Assisted("sequence") int sequence,
									@Assisted("parent") XWindow parent,
									@Assisted("window") XWindow window);

	XMotionEvent createXMotionEvent();

	XNoExposeEvent createXNoExposeEvent();

			XPointerVisitationEvent
			createXPointerVisitationEvent(	@Assisted("eventCode") int eventCode,
											@Assisted("detail") int detail,
											@Assisted("sequence") int sequence,
											@Assisted("time") int time,
											@Assisted("root") XWindow root,
											@Assisted("event") XWindow event,
											@Assisted("child") XWindow child,
											@Assisted("rootX") int rootX,
											@Assisted("rootY") int rootY,
											@Assisted("eventX") int eventX,
											@Assisted("eventY") int eventY,
											@Assisted("state") int state,
											@Assisted("mode") int mode,
											@Assisted boolean sameScreen);

	XPropertyEvent createXPropertyEvent(@Assisted("eventCode") int eventCode,
										@Assisted("sequence") int sequence,
										@Assisted XWindow window,
										@Assisted XAtom atom,
										@Assisted("time") int time,
										@Assisted boolean state);

	XReparentEvent createXReparentEvent();

	XResizeRequestEvent createXResizeRequestEvent();

	XSelectionClearEvent
			createXSelectionClearEvent(	@Assisted("eventCode") int eventCode,
										@Assisted("sequence") int sequence,
										@Assisted("time") int time,
										@Assisted XWindow owner,
										@Assisted XAtom selection);

	XSelectionEvent
			createXSelectionEvent(	@Assisted("eventCode") int eventCode,
									@Assisted("sequence") int sequence,
									@Assisted("time") int time,
									@Assisted XWindow requestor,
									@Assisted("selection") XAtom selection,
									@Assisted("target") XAtom target,
									@Assisted("property") XAtom property);

			XSelectionRequestEvent
			createXSelectionRequestEvent(	@Assisted("eventCode") int eventCode,
											@Assisted("sequence") int sequence,
											@Assisted("time") int time,
											@Assisted("owner") XWindow owner,
											@Assisted("requestor") XWindow requestor,
											@Assisted("selection") XAtom selection,
											@Assisted("target") XAtom target,
											@Assisted("property") XAtom property);

	XUnmapEvent createXUnmapEvent(	@Assisted("eventCode") int eventCode,
									@Assisted("sequence") int sequence,
									@Assisted("event") XWindow event,
									@Assisted("window") XWindow window,
									@Assisted boolean fromConfigure);

	XVisibilityEvent createXVisibilityEvent();

}
