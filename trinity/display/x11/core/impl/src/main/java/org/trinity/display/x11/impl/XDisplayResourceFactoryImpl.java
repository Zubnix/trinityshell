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
package org.trinity.display.x11.impl;

import java.util.HashMap;
import java.util.Map;

import org.trinity.display.x11.api.core.XCall;
import org.trinity.display.x11.api.core.XCaller;
import org.trinity.display.x11.api.core.XColormap;
import org.trinity.display.x11.api.core.XConnection;
import org.trinity.display.x11.api.core.XDisplayResourceFactory;
import org.trinity.display.x11.api.core.XDisplayServer;
import org.trinity.display.x11.api.core.XResourceHandle;
import org.trinity.display.x11.api.core.XVisual;
import org.trinity.display.x11.api.core.XWindow;
import org.trinity.display.x11.api.core.XWindowAttributes;
import org.trinity.display.x11.api.core.XWindowGeometry;
import org.trinity.foundation.display.api.ResourceHandle;
import org.trinity.foundation.shared.geometry.api.Coordinates;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/*****************************************
 * @author Erik De Rijcke
 * 
 ****************************************/
@Singleton
public class XDisplayResourceFactoryImpl implements XDisplayResourceFactory {

	private final Map<XResourceHandle, XWindow> xWindows = new HashMap<XResourceHandle, XWindow>();

	private final XCaller xCaller;

	private final XCall<Void, Long, Integer> addToSaveSet;
	private final XCall<Void, Long, Integer> destroyWindow;
	private final XCall<Void, Long, Number> enableEvents;
	private final XCall<Void, Long, Integer> focusWindow;
	private final XCall<XWindowAttributes, Long, Integer> getWindowAttributes;
	private final XCall<XWindowGeometry, Long, Integer> getWindowGeometry;
	private final XCall<Void, Long, Integer> grabButton;
	private final XCall<Void, Long, Number> grabKey;
	private final XCall<Void, Long, Integer> grabKeyboard;
	private final XCall<Void, Long, Integer> grabMouse;
	private final XCall<Void, Long, Integer> lowerWindow;
	private final XCall<Void, Long, Integer> mapWindow;
	private final XCall<Void, Long, Integer> moveResizeWindow;
	private final XCall<Void, Long, Integer> moveWindow;
	private final XCall<Void, Long, Object> overrideRedirectWindow;
	private final XCall<Void, Long, Integer> raiseWindow;
	private final XCall<Void, Long, Integer> removeFromSaveSet;
	private final XCall<Void, Long, Integer> reparentWindow;
	private final XCall<Void, Long, Integer> resizeWindow;
	// private final XCall<Void, Long, Object> sendClientMessage;
	private final XCall<Coordinates, Long, Integer> translateCoordinates;
	private final XCall<Void, Long, Integer> ungrabButton;
	private final XCall<Void, Long, Integer> ungrabKey;
	private final XCall<Void, Long, Integer> ungrabKeyboard;
	private final XCall<Void, Long, Integer> ungrabMouse;
	private final XCall<Void, Long, Integer> unmapWindow;

	private final XConnection<Long> xConnection;
	private final XDisplayServer display;

	@Inject
	public XDisplayResourceFactoryImpl(	final XDisplayServer display,
										final XConnection<Long> xConnection,
										final XCaller xCaller,
										@Named("addToSaveSet") final XCall<Void, Long, Integer> addToSaveSet,
										@Named("destroyWindow") final XCall<Void, Long, Integer> destroyWindow,
										@Named("enableEvents") final XCall<Void, Long, Number> enableEvents,
										@Named("focusWindow") final XCall<Void, Long, Integer> focusWindow,
										@Named("getWindowAttributes") final XCall<XWindowAttributes, Long, Integer> getWindowAttributes,
										@Named("getWindowGeometry") final XCall<XWindowGeometry, Long, Integer> getWindowGeometry,
										@Named("grabButton") final XCall<Void, Long, Integer> grabButton,
										@Named("grabKey") final XCall<Void, Long, Number> grabKey,
										@Named("grabKeyboard") final XCall<Void, Long, Integer> grabKeyboard,
										@Named("grabMouse") final XCall<Void, Long, Integer> grabMouse,
										@Named("lowerWindow") final XCall<Void, Long, Integer> lowerWindow,
										@Named("mapWindow") final XCall<Void, Long, Integer> mapWindow,
										@Named("moveResizeWindow") final XCall<Void, Long, Integer> moveResizeWindow,
										@Named("moveWindow") final XCall<Void, Long, Integer> moveWindow,
										@Named("overrideRedirectWindow") final XCall<Void, Long, Object> overrideRedirectWindow,
										@Named("raiseWindow") final XCall<Void, Long, Integer> raiseWindow,
										@Named("removeFromSaveSet") final XCall<Void, Long, Integer> removeFromSaveSet,
										@Named("reparentWindow") final XCall<Void, Long, Integer> reparentWindow,
										@Named("resizeWindow") final XCall<Void, Long, Integer> resizeWindow,
										// @Named("sendClientMessage") final
										// XCall<Void, Long, Object>
										// sendClientMessage,
										@Named("translateCoordinates") final XCall<Coordinates, Long, Integer> translateCoordinates,
										@Named("ungrabButton") final XCall<Void, Long, Integer> ungrabButton,
										@Named("ungrabKey") final XCall<Void, Long, Integer> ungrabKey,
										@Named("ungrabKeyboard") final XCall<Void, Long, Integer> ungrabKeyboard,
										@Named("ungrabMouse") final XCall<Void, Long, Integer> ungrabMouse,
										@Named("unmapWindow") final XCall<Void, Long, Integer> unmapWindow) {
		this.display = display;
		this.xConnection = xConnection;
		this.xCaller = xCaller;
		this.addToSaveSet = addToSaveSet;
		this.destroyWindow = destroyWindow;
		this.enableEvents = enableEvents;
		this.focusWindow = focusWindow;
		this.getWindowAttributes = getWindowAttributes;
		this.getWindowGeometry = getWindowGeometry;
		this.grabButton = grabButton;
		this.grabKey = grabKey;
		this.grabKeyboard = grabKeyboard;
		this.grabMouse = grabMouse;
		this.lowerWindow = lowerWindow;
		this.mapWindow = mapWindow;
		this.moveResizeWindow = moveResizeWindow;
		this.moveWindow = moveWindow;
		this.overrideRedirectWindow = overrideRedirectWindow;
		this.raiseWindow = raiseWindow;
		this.removeFromSaveSet = removeFromSaveSet;
		this.reparentWindow = reparentWindow;
		this.resizeWindow = resizeWindow;
		// this.sendClientMessage = sendClientMessage;
		this.translateCoordinates = translateCoordinates;
		this.ungrabButton = ungrabButton;
		this.ungrabKey = ungrabKey;
		this.ungrabKeyboard = ungrabKeyboard;
		this.ungrabMouse = ungrabMouse;
		this.unmapWindow = unmapWindow;
	}

	@Override
	public XWindow
			createPlatformRenderArea(final ResourceHandle resourceHandle) {
		// TODO Auto-generated method stub
		XWindow xWindow = this.xWindows.get(resourceHandle);
		if (xWindow == null) {
			xWindow = new XWindowImpl(	this.display,
										this.xConnection,
										this.xCaller,
										this.addToSaveSet,
										this.destroyWindow,
										this.enableEvents,
										this.focusWindow,
										this.getWindowAttributes,
										this.getWindowGeometry,
										this.grabButton,
										this.grabKey,
										this.grabKeyboard,
										this.grabMouse,
										this.lowerWindow,
										this.mapWindow,
										this.moveResizeWindow,
										this.moveWindow,
										this.overrideRedirectWindow,
										this.raiseWindow,
										this.removeFromSaveSet,
										this.reparentWindow,
										this.resizeWindow,
										// this.sendClientMessage,
										this.translateCoordinates,
										this.ungrabButton,
										this.ungrabKey,
										this.ungrabKeyboard,
										this.ungrabMouse,
										this.unmapWindow,
										(XResourceHandle) resourceHandle);
		}
		return xWindow;
	}

	@Override
	public XVisual createXVisual(final XResourceHandle xResourceHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XColormap createXColormap(final XResourceHandle xResourceHandle) {
		// TODO Auto-generated method stub
		return null;
	}

}
