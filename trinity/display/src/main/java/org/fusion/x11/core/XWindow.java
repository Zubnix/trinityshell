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
package org.fusion.x11.core;

import java.util.HashMap;
import java.util.Map;

import org.trinity.core.display.api.Atom;
import org.trinity.core.display.api.PlatformRenderArea;
import org.trinity.core.display.api.PlatformRenderAreaAttributes;
import org.trinity.core.display.api.Property;
import org.trinity.core.display.api.PropertyInstance;
import org.trinity.core.display.api.event.ClientMessageEvent;
import org.trinity.core.display.impl.EventPropagator;
import org.trinity.core.geometry.api.Coordinates;
import org.trinity.core.input.api.InputModifiers;
import org.trinity.core.input.api.Key;
import org.trinity.core.input.impl.BaseButton;
import org.trinity.core.input.impl.BaseInputModifiers;
import org.trinity.core.input.impl.BaseKey;

/**
 * An <code>XWindow</code> represents a native X window. This implies that a
 * <code>XWindow</code> is actually a proxy to a native X window. To address the
 * correct native X window, a <code>XWindow</code> has a <code>XID</code>. This
 * <code>XID</code> holds both the window id and the display of the target
 * native X window.
 * <p>
 * A <code>XWindow</code> can be used to manipulate the native X window that it
 * represents. Manipulation of a <code>XWindow</code> is limited to non painting
 * operations.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XWindow extends XResource implements PlatformRenderArea,
		XDrawable {
	// TODO clean up resources (ie certain xwindow relations, xresource
	// registry, ...) if the render area on the display server that is
	// represented by this XWindow is destroyed.

	private final XCoreInterface xCoreInterface;

	private final Map<Atom, Boolean> propertyValidityCache;
	private final Map<Property<? extends PropertyInstance>, PropertyInstance> cachedPropertyReplies;

	/**
	 * Create a new <code>XWindow</code> identified by the given
	 * <code>XID</code>. The given <code>XID</code> uniquely identifies the
	 * created <code>XWindow</code> on a native display.
	 * 
	 * @param xid
	 *            An {@link XID}.
	 * 
	 */
	XWindow(final XID xid) {
		super(xid);
		this.xCoreInterface = xid.getDisplay().getXCoreInterface();

		this.cachedPropertyReplies = new HashMap<Property<? extends PropertyInstance>, PropertyInstance>();
		this.propertyValidityCache = new HashMap<Atom, Boolean>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends PropertyInstance> T getPropertyInstance(
			final Property<T> property) {
		T propertyReply;
		if (isPropertyValid(property)) {
			propertyReply = (T) this.cachedPropertyReplies.get(property);
		} else {
			propertyReply = property.getPropertyInstance(this);
			this.cachedPropertyReplies.put(property, propertyReply);
			this.propertyValidityCache.put(property, Boolean.valueOf(true));
		}
		return propertyReply;
	}

	/**
	 * Set the cached value of the given property type, represented by the given
	 * <code>XAtom</code>, as invalid. The cache will be valid again after it is
	 * refreshed by a call to <code>getPropertyReply</code>.
	 * <p>
	 * A property can be invalidated by either a server generated
	 * <code>PropertyChangedNotifyEvent</code> which will call this method, or a
	 * manual call to this method.
	 * 
	 * @param property
	 *            The property type to invalidate.
	 * 
	 */
	public void invalidateProperty(
			final XPropertyXAtom<? extends PropertyInstance> property) {
		if (property == null) {
			return;
		}
		this.propertyValidityCache.put(property, Boolean.valueOf(false));
	}

	/**
	 * Check if the cached value of the given property type, represented by the
	 * given <code>PropertyXAtom</code>, is valid.
	 * 
	 * @param property
	 *            The property type to check.
	 * @return True if valid, false if not.
	 */
	private boolean isPropertyValid(
			final Property<? extends PropertyInstance> property) {
		boolean valid;
		if (this.propertyValidityCache.containsKey(property)) {
			valid = this.propertyValidityCache.get(property).booleanValue();
		} else {
			valid = false;
		}
		return valid;
	}

	@Override
	public void destroy() {
		getXCoreInterface().forceDestroyWindow(this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XResource) {
			return ((XResource) obj).getDisplayResourceHandle().equals(
					getDisplayResourceHandle());
		}
		return false;
	}

	@Override
	public PlatformRenderArea[] getChildren() {
		final PlatformRenderArea[] returnPlatformRenderAreaArray = getXCoreInterface()
				.getChildWindows(this);
		return returnPlatformRenderAreaArray;
	}

	/**
	 * The <code>XCoreInterface</code> that is used by this <code>XWindow</code>
	 * to talk to the display server.
	 * 
	 * @return An {@link XCoreInterface}.
	 */
	public XCoreInterface getXCoreInterface() {
		return this.xCoreInterface;
	}

	@Override
	public void setInputFocus() {
		getXCoreInterface().focusWindow(this);
	}

	@Override
	public int hashCode() {
		return getDisplayResourceHandle().hashCode();
	}

	@Override
	public void lower() {
		getXCoreInterface().lowerWindow(this);
	}

	@Override
	public void show() {
		getXCoreInterface().mapWindow(this);
		// setWmState(WmStateEnum.NormalState,
		// getDisplayResourceHandle().getDisplay().getNoneWindow());
	}

	@Override
	public void move(final int x, final int y) {
		getXCoreInterface().moveWindow(this, x, y);
	}

	@Override
	public void moveResize(final int x, final int y, final int width,
			final int height) {
		getXCoreInterface().moveResizeWindow(this, x, y, width, height);
	}

	// @Override
	public void overrideRedirect(final boolean override) {
		getXCoreInterface().overrideRedirectWindow(this, override);
	}

	@Override
	public void propagateEvent(final EventPropagator... eventMasks) {
		getXCoreInterface().enableEvents(this, eventMasks);
	}

	@Override
	public XWindowGeometry getPlatformRenderAreaGeometry() {
		final XWindowGeometry returnWindowGeometry = getXCoreInterface()
				.getWindowGeometry(this);
		return returnWindowGeometry;
	}

	@Override
	public void raise() {
		getXCoreInterface().raiseWindow(this);
	}

	@Override
	public PlatformRenderAreaAttributes getPlatformRenderAreaAttributes() {
		final PlatformRenderAreaAttributes returnPlatformRenderAreaAttributes = getXCoreInterface()
				.getWindowAttributesCopy(this);
		return returnPlatformRenderAreaAttributes;
	}

	@Override
	public void setParent(final PlatformRenderArea parent, final int x,
			final int y) {
		getXCoreInterface().reparentWindow((XWindow) parent, this, x, y);
	}

	@Override
	public void resize(final int width, final int height) {
		getXCoreInterface().resizeWindow(this, width, height);
	}

	@Override
	public void hide() {
		getXCoreInterface().unmapWindow(this);
	}

	@Override
	public void sendMessage(final ClientMessageEvent clientMessageEvent) {
		getXCoreInterface().sendClientMessage(this, clientMessageEvent);
	}

	@Override
	public void catchKeyboardInput(final Key catchKey,
			final InputModifiers withModifiers) {
		getXCoreInterface().grabKey(this, catchKey, withModifiers);
	}

	@Override
	public <T extends PropertyInstance> void setPropertyInstance(
			final Property<T> property, final T propertyInstance) {
		property.setPropertyInstance(this, propertyInstance);
	}

	@Override
	public void catchMouseInput(final BaseButton catchButton,
			final BaseInputModifiers withModifiers) {
		getXCoreInterface().grabButton(this, catchButton, withModifiers);
	}

	@Override
	public void disableKeyboardInputCatching(final BaseKey catchKey,
			final BaseInputModifiers withModifiers) {
		getXCoreInterface().ungrabKey(this, catchKey, withModifiers);
	}

	@Override
	public void disableMouseInputCatching(final BaseButton catchButton,
			final BaseInputModifiers withModifiers) {
		getXCoreInterface().ungrabButton(this, catchButton, withModifiers);
	}

	@Override
	public void catchAllKeyboardInput() {
		getXCoreInterface().grabKeyboard(this);
	}

	@Override
	public void catchAllMouseInput() {
		getXCoreInterface().grabMouse(this);
	}

	@Override
	public void stopKeyboardInputCatching() {
		getXCoreInterface().ungrabKeybard(this);
	}

	@Override
	public void stopMouseInputCatching() {
		getXCoreInterface().ungrabMouse(this);
	}

	public void addToSaveSet() {
		getXCoreInterface().addToSaveSet(this);
	}

	public void removeFromSaveSet() {
		getXCoreInterface().removeFromSaveSet(this);
	}

	@Override
	public Coordinates translateCoordinates(final PlatformRenderArea source,
			final int sourceX, final int sourceY) {
		final Coordinates coordinates = getXCoreInterface()
				.translateCoordinates(this, (XWindow) source, sourceX, sourceY);
		return coordinates;
	}
}
