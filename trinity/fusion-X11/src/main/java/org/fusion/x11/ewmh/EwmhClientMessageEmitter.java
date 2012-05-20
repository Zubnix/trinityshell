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
package org.fusion.x11.ewmh;

import java.util.HashMap;
import java.util.Map;

import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XDisplay;
import org.hydrogen.display.api.Atom;
import org.hydrogen.display.api.event.ClientMessageEvent;
import org.hydrogen.event.api.EventHandler;
import org.hydrogen.event.api.base.EventBus;


//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class EwmhClientMessageEmitter extends EventBus implements
		EventHandler<ClientMessageEvent> {

	private interface EwmhClientMessageWrapper {
		EwmhClientMessageEvent wrapClientMessage(
				ClientMessageEvent clientMessageEvent);
	}

	private final class NetActiveWindowMessageWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetActiveWindowMessage(getDisplay(), clientMessageEvent);
		}
	}

	private final class NetCloseWindowMessageWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetCloseWindowMessage(getDisplay(), clientMessageEvent);
		}
	}

	private final class NetCurrentDesktopWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetCurrentDesktopMessage(clientMessageEvent);
		}
	}

	private final class NetDesktopGeometryWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetDesktopGeometryMessage(clientMessageEvent);
		}
	}

	private final class NetDesktopViewportWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetDesktopViewPortMessage(clientMessageEvent);
		}
	}

	private final class NetMoveResizeWindowWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetMoveResizeWindowMessage(clientMessageEvent);
		}
	}

	private final class NetNumberOfDesktopsWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetNumberOfDesktopsMessage(clientMessageEvent);
		}
	}

	private final class NetRequestFrameExtendsWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetRequestFrameExtentsMessage(clientMessageEvent);
		}
	}

	private final class NetRestackWindowWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetRestackWindowMessage(clientMessageEvent);
		}
	}

	private final class NetShowingDesktopWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetShowingDesktopMessage(clientMessageEvent);
		}
	}

	private final class NetWmDesktopWrapper implements EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetWmDesktopMessage(clientMessageEvent);
		}
	}

	private final class NetWmFullscreenMonitorsWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetWmFullscreenMonitorsMessage(clientMessageEvent);
		}
	}

	private final class NetWmMoveResizeWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetWmMoveResizeMessage(getDisplay(), clientMessageEvent);
		}
	}

	private final class NetWmPingWrapper implements EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetWmPingMessage(getDisplay(), clientMessageEvent);
		}
	}

	private final class NetWmStateWrapper implements EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetWmStateMessage(getDisplay(), clientMessageEvent);
		}
	}

	private final class NetWmSyncRequestWrapper implements
			EwmhClientMessageWrapper {
		@Override
		public EwmhClientMessageEvent wrapClientMessage(
				final ClientMessageEvent clientMessageEvent) {
			return new _NetWmSyncRequestMessage(getDisplay(),
					clientMessageEvent);
		}
	}

	private final Map<Atom, EwmhClientMessageWrapper> ewmhClientMessageWrappers;
	private final XDisplay display;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public EwmhClientMessageEmitter(final XDisplay display) {
		this.display = display;
		this.ewmhClientMessageWrappers = new HashMap<Atom, EwmhClientMessageEmitter.EwmhClientMessageWrapper>(
				16);
		initEmwhClientMessageWrappers();
	}

	/**
	 * 
	 */
	protected void initEmwhClientMessageWrappers() {
		// TODO refactor get atom names from EwmhAtoms

		final XAtom netActiveWindow = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_ACTIVE_WINDOW");
		getEwmhClientMessageWrappers().put(netActiveWindow,
				new NetActiveWindowMessageWrapper());

		final XAtom netCloseWindow = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_CLOSE_WINDOW");
		getEwmhClientMessageWrappers().put(netCloseWindow,
				new NetCloseWindowMessageWrapper());

		final XAtom netCurrentDesktop = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_CURRENT_DESKTOP");
		getEwmhClientMessageWrappers().put(netCurrentDesktop,
				new NetCurrentDesktopWrapper());

		final XAtom netDesktopGeometry = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_DESKTOP_GEOMETRY");
		getEwmhClientMessageWrappers().put(netDesktopGeometry,
				new NetDesktopGeometryWrapper());

		final XAtom netDesktopViewport = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_DESKTOP_VIEWPORT");
		getEwmhClientMessageWrappers().put(netDesktopViewport,
				new NetDesktopViewportWrapper());

		final XAtom netMoveresizeWindow = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_MOVERESIZE_WINDOW");
		getEwmhClientMessageWrappers().put(netMoveresizeWindow,
				new NetMoveResizeWindowWrapper());

		final XAtom netNumberOfDesktops = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_NUMBER_OF_DESKTOPS");
		getEwmhClientMessageWrappers().put(netNumberOfDesktops,
				new NetNumberOfDesktopsWrapper());

		final XAtom netRequestFrameExtents = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_REQUEST_FRAME_EXTENTS");
		getEwmhClientMessageWrappers().put(netRequestFrameExtents,
				new NetRequestFrameExtendsWrapper());

		final XAtom netRestackWindow = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_RESTACK_WINDOW");
		getEwmhClientMessageWrappers().put(netRestackWindow,
				new NetRestackWindowWrapper());

		final XAtom netShowingDesktop = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_SHOWING_DESKTOP");
		getEwmhClientMessageWrappers().put(netShowingDesktop,
				new NetShowingDesktopWrapper());

		final XAtom netWmDesktop = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_WM_DESKTOP");
		getEwmhClientMessageWrappers().put(netWmDesktop,
				new NetWmDesktopWrapper());

		final XAtom netWmFullscreenMonitors = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_WM_FULLSCREEN_MONITORS");
		getEwmhClientMessageWrappers().put(netWmFullscreenMonitors,
				new NetWmFullscreenMonitorsWrapper());

		final XAtom netWmMoveresize = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_WM_MOVERESIZE");
		getEwmhClientMessageWrappers().put(netWmMoveresize,
				new NetWmMoveResizeWrapper());

		final XAtom netWmPing = getDisplay().getDisplayAtoms().getAtomByName(
				"_NET_WM_PING");
		getEwmhClientMessageWrappers().put(netWmPing, new NetWmPingWrapper());

		final XAtom netWmState = getDisplay().getDisplayAtoms().getAtomByName(
				"_NET_WM_STATE");
		getEwmhClientMessageWrappers().put(netWmState, new NetWmStateWrapper());

		final XAtom netWmSyncRequest = getDisplay().getDisplayAtoms()
				.getAtomByName("_NET_WM_SYNC_REQUEST");
		getEwmhClientMessageWrappers().put(netWmSyncRequest,
				new NetWmSyncRequestWrapper());
	}

	private Map<Atom, EwmhClientMessageWrapper> getEwmhClientMessageWrappers() {
		return this.ewmhClientMessageWrappers;
	}

	/**
	 * 
	 * @return
	 */
	public XDisplay getDisplay() {
		return this.display;
	}

	@Override
	public void handleEvent(final ClientMessageEvent event) {
		final Atom messageType = event.getMessageType();
		if (messageType != null) {
			final EwmhClientMessageWrapper clientMessageWrapper = getEwmhClientMessageWrappers()
					.get(messageType);
			if (clientMessageWrapper != null) {
				final EwmhClientMessageEvent ewmhClientMessageEvent = clientMessageWrapper
						.wrapClientMessage(event);
				// TODO find a way to fire an ewmh client message event to a
				// listener. This requires an event type with an xatom as it's
				// 'type' id and a corresponding handler with the
				// ewmhclientmessage as the event to handle.
				fireEvent(ewmhClientMessageEvent);
			}
		}
	}
}
