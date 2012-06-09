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
package org.fusion.x11.icccm;

import org.fusion.x11.core.XAtom;
import org.trinity.core.event.api.EventHandler;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XWindowImpl;
import org.trinity.display.x11.impl.event.XSelectionClearNotifyEvent;
import org.trinity.display.x11.impl.event.XSelectionRequestEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class SelectionManager {

	private final XWindowImpl icccmSelectionOwner;
	private final IcccmAtoms icccmAtoms;
	private final XServerImpl display;

	SelectionManager(final IcccmAtoms icccmAtoms) {
		this.display = icccmAtoms.getDisplay();
		this.icccmAtoms = icccmAtoms;
		// initialize the actual selection owner window
		this.icccmSelectionOwner = getDisplay().getXCoreInterface()
				.createNewWindow(getDisplay());
	}

	// TODO when losing the selection, dont listen for events anymore on the
	// display and close the selection owner window.

	// TODO selection clear

	private void listenForSelectionEvents() {

		// TODO don't depend directly on Xcb events, split in seperate classes
		getDisplay().addEventHandler(
				new EventHandler<XSelectionClearNotifyEvent>() {
					@Override
					public void handleEvent(
							final XSelectionClearNotifyEvent event) {
						// TODO handle someone else taking away our screen
						// selection.
					}
				}, XSelectionClearNotifyEvent.TYPE);

		// not needed unless we request selection
		// conversions ourself
		// getDisplay().addEventHandler(
		// new EventHandler<XcbSelectionNotifyEvent>() {
		// @Override
		// public void handleEvent(final XcbSelectionNotifyEvent event) {
		//
		// }
		// }, XcbSelectionNotifyEvent.TYPE);

		getDisplay().addEventHandler(
				new EventHandler<XSelectionRequestEvent>() {
					@Override
					public void handleEvent(final XSelectionRequestEvent event) {
						handleSelectionRequest(event);
					}
				}, XSelectionRequestEvent.TYPE);
	}

	private void handleSelectionRequest(
			final XSelectionRequestEvent selectionRequestEvent) {

		final XWindowImpl selectionOwner = selectionRequestEvent.getEventSource();
		final XWindowImpl requestingClient = selectionRequestEvent.getRequestor();
		final XAtom requestedSelectionAtom = selectionRequestEvent
				.getSelection();
		final XAtom targetAtom = selectionRequestEvent.getTarget();

		// TODO we want to handle more selection conversion requests. Refactor
		// to properly handle this.

		final boolean correctSelectionOwner = (selectionOwner == getIcccmSelectionOwner());
		final boolean correctSelectionAtom = (requestedSelectionAtom == getIcccmAtoms()
				.getScreenSelectionAtom(0));
		final Version version = getIcccmAtoms().getVersion();
		final boolean correctTargetType = (targetAtom == version);

		if (correctSelectionOwner && correctSelectionAtom && correctTargetType) {
			requestingClient.setPropertyInstance(version, new VersionInstance(
					getDisplay(), Icccm.MAJOR, Icccm.MINOR));
		}
	}

	public boolean isScreenSelectionAvailable() {
		// TODO how to determine screen number? default to 0 for now
		final XAtom selectionAtom = getIcccmAtoms().getScreenSelectionAtom(0);
		final XWindowImpl owner = getDisplay().getXCoreInterface()
				.getSelectionOwner(selectionAtom);
		return owner == getDisplay().getNoneWindow();
	}

	public void initScreenSelection() {
		// Set the icccm selection owner for screen. How to determine screen
		// number? For now default to 0.
		final XAtom wmS0 = getIcccmAtoms().getScreenSelectionAtom(0);
		getDisplay().getXCoreInterface().setSelectionOwner(wmS0,
				getIcccmSelectionOwner());
		listenForSelectionEvents();
	}

	public XServerImpl getDisplay() {
		return this.display;
	}

	public IcccmAtoms getIcccmAtoms() {
		return this.icccmAtoms;
	}

	public XWindowImpl getIcccmSelectionOwner() {
		return this.icccmSelectionOwner;
	}
}
