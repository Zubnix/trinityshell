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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hydrogen.displayinterface.Atom;
import org.hydrogen.displayinterface.EventPropagator;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PlatformRenderAreaAttributes;
import org.hydrogen.displayinterface.PlatformRenderAreaRelation;
import org.hydrogen.displayinterface.PlatformRenderAreaType;
import org.hydrogen.displayinterface.Property;
import org.hydrogen.displayinterface.PropertyInstance;
import org.hydrogen.displayinterface.event.ClientMessageEvent;
import org.hydrogen.displayinterface.input.Button;
import org.hydrogen.displayinterface.input.InputModifiers;
import org.hydrogen.displayinterface.input.Key;

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

	private PlatformRenderAreaType platformRenderAreaType;
	private final Set<XWindowRelation> relations;

	private final XWindowPreferences preferences;

	// private final IcccmProtocolDelegate icccmProtocolDelegate;

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
		this.preferences = new XWindowPreferences();
		this.relations = new HashSet<XWindowRelation>();
		setPlatformRenderAreaType(PlatformRenderAreaType.UNKNOWN_RENDER_AREA);
		this.xCoreInterface = xid.getDisplay().getXCoreInterface();

		this.cachedPropertyReplies = new HashMap<Property<? extends PropertyInstance>, PropertyInstance>();
		this.propertyValidityCache = new HashMap<Atom, Boolean>();
	}

	@Override
	public PlatformRenderAreaType getPlatformRenderAreaType() {
		return this.platformRenderAreaType;
	}

	/**
	 * The given <code>PlatformRenderAreaType</code> will identify this
	 * <code>XWindow</code> purpose.
	 * 
	 * @param platformRenderAreaType
	 *            The <code>PlatformRenderAreaType</code> for this
	 *            <code>XWindow</code>
	 */
	public void setPlatformRenderAreaType(
			final PlatformRenderAreaType platformRenderAreaType) {
		this.platformRenderAreaType = platformRenderAreaType;
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

	// /**
	// * ICCCM property.
	// * <p>
	// * Set the WM State on this <code>XWindow</code> so the client that
	// created
	// * this <code>XWindow</code> is aware in what state this
	// * <code>XWindow</code> is in.
	// *
	// * @param wmState
	// * @param iconXWindow
	// * @
	// */
	// public void setWmState(final WmStateEnum wmState,
	// final XWindow iconXWindow)
	// {
	//
	// final XDisplay display = getDisplayResourceHandle().getDisplay();
	//
	// setPropertyInstance(display.getProperties().WM_STATE,
	// new WmStateInstance(display,
	// wmState,
	// iconXWindow));
	// }

	@Override
	public void move(final int x, final int y) {
		getXCoreInterface().moveWindow(this, x, y);
	}

	@Override
	public void moveResize(final int x, final int y, final int width,
			final int height) {
		getXCoreInterface().moveResizeWindow(this, x, y, width, height);
	}

	@Override
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
	public void requestDestroy() {
		getXCoreInterface().requestDestroyWindow(this);
	}

	@Override
	public void resize(final int width, final int height) {
		getXCoreInterface().resizeWindow(this, width, height);
	}

	@Override
	public void hide() {
		getXCoreInterface().unmapWindow(this);
		// setWmState(WmStateEnum.WithdrawnState,
		// getDisplayResourceHandle().getDisplay().getNoneWindow());
	}

	@Override
	public void sendMessage(final ClientMessageEvent clientMessageEvent) {
		getXCoreInterface().sendClientMessage(this, clientMessageEvent);
	}

	/**
	 * Adds a <code>XWindow</code> that has a certain relation to this
	 * <code>XWindow</code>. For example the added <code>XWindow</code> can be a
	 * popup on behalve of this <code>XWindow</code>.
	 * <p>
	 * A call to <code>getPlatformRenderAreaType</code> might give a hint as how
	 * the <code>XWindow</code>s are related.
	 * 
	 * @param relation
	 *            The <code>XWindow</code> that has a relation to this
	 *            <code>XWindow</code>.
	 */
	public void addXWindowRelation(final XWindowRelation relation) {
		this.relations.add(relation);
	}

	@Override
	public PlatformRenderAreaRelation[] getPlatformRenderAreaRelations() {
		return this.relations
				.toArray(new XWindowRelation[this.relations.size()]);
	}

	@Override
	public XWindowPreferences getPreferences() {
		return this.preferences;
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
	public void catchMouseInput(final Button catchButton,
			final InputModifiers withModifiers) {
		getXCoreInterface().grabButton(this, catchButton, withModifiers);
	}

	@Override
	public void disableKeyboardInputCatching(final Key catchKey,
			final InputModifiers withModifiers) {
		getXCoreInterface().ungrabKey(this, catchKey, withModifiers);
	}

	@Override
	public void disableMouseInputCatching(final Button catchButton,
			final InputModifiers withModifiers) {
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
}
