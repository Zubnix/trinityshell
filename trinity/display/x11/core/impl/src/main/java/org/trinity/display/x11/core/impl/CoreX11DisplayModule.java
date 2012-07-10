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
package org.trinity.display.x11.core.impl;

import org.trinity.display.x11.core.api.XAtom;
import org.trinity.display.x11.core.api.XAtomFactory;
import org.trinity.display.x11.core.api.XDisplayResourceFactory;
import org.trinity.display.x11.core.api.XResourceHandle;
import org.trinity.display.x11.core.api.XResourceHandleFactory;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.display.x11.core.api.event.XButtonEvent;
import org.trinity.display.x11.core.api.event.XCirculateEvent;
import org.trinity.display.x11.core.api.event.XCirculateRequestEvent;
import org.trinity.display.x11.core.api.event.XClientMessageEvent;
import org.trinity.display.x11.core.api.event.XColormapEvent;
import org.trinity.display.x11.core.api.event.XConfigureEvent;
import org.trinity.display.x11.core.api.event.XConfigureRequestEvent;
import org.trinity.display.x11.core.api.event.XCreateWindowEvent;
import org.trinity.display.x11.core.api.event.XCrossingEvent;
import org.trinity.display.x11.core.api.event.XDestroyWindowEvent;
import org.trinity.display.x11.core.api.event.XEventFactory;
import org.trinity.display.x11.core.api.event.XExposeEvent;
import org.trinity.display.x11.core.api.event.XFocusEvent;
import org.trinity.display.x11.core.api.event.XGraphicsExposeEvent;
import org.trinity.display.x11.core.api.event.XGravityEvent;
import org.trinity.display.x11.core.api.event.XKeyEvent;
import org.trinity.display.x11.core.api.event.XMapEvent;
import org.trinity.display.x11.core.api.event.XMapRequestEvent;
import org.trinity.display.x11.core.api.event.XMotionEvent;
import org.trinity.display.x11.core.api.event.XNoExposeEvent;
import org.trinity.display.x11.core.api.event.XPointerVisitationEvent;
import org.trinity.display.x11.core.api.event.XPropertyEvent;
import org.trinity.display.x11.core.api.event.XReparentEvent;
import org.trinity.display.x11.core.api.event.XResizeRequestEvent;
import org.trinity.display.x11.core.api.event.XSelectionClearEvent;
import org.trinity.display.x11.core.api.event.XSelectionEvent;
import org.trinity.display.x11.core.api.event.XSelectionRequestEvent;
import org.trinity.display.x11.core.api.event.XUnmapEvent;
import org.trinity.display.x11.core.api.event.XVisibilityEvent;
import org.trinity.display.x11.core.impl.event.XButtonEventImpl;
import org.trinity.display.x11.core.impl.event.XCirculateEventImpl;
import org.trinity.display.x11.core.impl.event.XCirculateRequestEventImpl;
import org.trinity.display.x11.core.impl.event.XClientMessageEventImpl;
import org.trinity.display.x11.core.impl.event.XColormapEventImpl;
import org.trinity.display.x11.core.impl.event.XConfigureEventImpl;
import org.trinity.display.x11.core.impl.event.XConfigureRequestEventImpl;
import org.trinity.display.x11.core.impl.event.XCreateWindowEventImpl;
import org.trinity.display.x11.core.impl.event.XCrossingEventImpl;
import org.trinity.display.x11.core.impl.event.XDestroyWindowEventImpl;
import org.trinity.display.x11.core.impl.event.XExposeEventImpl;
import org.trinity.display.x11.core.impl.event.XFocusEventImpl;
import org.trinity.display.x11.core.impl.event.XGraphicsExposeEventImpl;
import org.trinity.display.x11.core.impl.event.XGravityEventImpl;
import org.trinity.display.x11.core.impl.event.XKeyEventImpl;
import org.trinity.display.x11.core.impl.event.XMapEventImpl;
import org.trinity.display.x11.core.impl.event.XMapRequestEventImpl;
import org.trinity.display.x11.core.impl.event.XMotionEventImpl;
import org.trinity.display.x11.core.impl.event.XNoExposeEventImpl;
import org.trinity.display.x11.core.impl.event.XPointerVisitationEventImpl;
import org.trinity.display.x11.core.impl.event.XPropertyEventImpl;
import org.trinity.display.x11.core.impl.event.XReparentEventImpl;
import org.trinity.display.x11.core.impl.event.XResizeRequestEventImpl;
import org.trinity.display.x11.core.impl.event.XSelectionClearEventImpl;
import org.trinity.display.x11.core.impl.event.XSelectionEventImpl;
import org.trinity.display.x11.core.impl.event.XSelectionRequestEventImpl;
import org.trinity.display.x11.core.impl.event.XUnmapEventImpl;
import org.trinity.display.x11.core.impl.event.XVisibilityEventImpl;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import de.devsurf.injection.guice.annotations.GuiceModule;

/*****************************************
 * @author Erik De Rijcke
 * 
 ****************************************/
@GuiceModule
public class CoreX11DisplayModule extends AbstractModule {

	@Override
	protected void configure() {
		bindCoreEvents();
		bindCore();
	}

	private void bindCoreEvents() {
		install(new FactoryModuleBuilder()
				.implement(XButtonEvent.class, XButtonEventImpl.class)
				.implement(XCirculateEvent.class, XCirculateEventImpl.class)
				.implement(	XCirculateRequestEvent.class,
							XCirculateRequestEventImpl.class)
				.implement(	XClientMessageEvent.class,
							XClientMessageEventImpl.class)
				.implement(XColormapEvent.class, XColormapEventImpl.class)
				.implement(XConfigureEvent.class, XConfigureEventImpl.class)
				.implement(	XConfigureRequestEvent.class,
							XConfigureRequestEventImpl.class)
				.implement(	XCreateWindowEvent.class,
							XCreateWindowEventImpl.class)
				.implement(XCrossingEvent.class, XCrossingEventImpl.class)
				.implement(	XDestroyWindowEvent.class,
							XDestroyWindowEventImpl.class)
				.implement(XExposeEvent.class, XExposeEventImpl.class)
				.implement(XFocusEvent.class, XFocusEventImpl.class)
				.implement(	XGraphicsExposeEvent.class,
							XGraphicsExposeEventImpl.class)
				.implement(XGravityEvent.class, XGravityEventImpl.class)
				.implement(XKeyEvent.class, XKeyEventImpl.class)
				.implement(XMapEvent.class, XMapEventImpl.class)
				.implement(XMapRequestEvent.class, XMapRequestEventImpl.class)
				.implement(XMotionEvent.class, XMotionEventImpl.class)
				.implement(XNoExposeEvent.class, XNoExposeEventImpl.class)
				.implement(	XPointerVisitationEvent.class,
							XPointerVisitationEventImpl.class)
				.implement(XPropertyEvent.class, XPropertyEventImpl.class)
				.implement(XReparentEvent.class, XReparentEventImpl.class)
				.implement(	XResizeRequestEvent.class,
							XResizeRequestEventImpl.class)
				.implement(	XSelectionClearEvent.class,
							XSelectionClearEventImpl.class)
				.implement(XSelectionEvent.class, XSelectionEventImpl.class)
				.implement(	XSelectionRequestEvent.class,
							XSelectionRequestEventImpl.class)
				.implement(XUnmapEvent.class, XUnmapEventImpl.class)
				.implement(XVisibilityEvent.class, XVisibilityEventImpl.class)
				.build(XEventFactory.class));
	}

	private void bindCore() {
		// bind(XCaller.class).to(XCallerImpl.class);
		// bind(XConnection.class).to(XConnectionImpl.class);
		// bind(XDisplayProtocol.class).to(XDisplayProtocolImpl.class);
		// bind(XDisplayServer.class).to(XDisplayServerImpl.class);
		// bind(Mouse.class).to(MouseImpl.class);

		install(new FactoryModuleBuilder().implement(	XWindow.class,
														XWindowImpl.class)
				.build(XDisplayResourceFactory.class));
		install(new FactoryModuleBuilder()
				.implement(XResourceHandle.class, XResourceHandleImpl.class)
				.build(XResourceHandleFactory.class));
		install(new FactoryModuleBuilder().implement(	XAtom.class,
														XAtomImpl.class)
				.build(XAtomFactory.class));
	}
}