/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.x11;

import org.apache.log4j.Logger;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceCardinal;
import org.fusion.x11.core.XPropertyInstanceCardinals;
import org.fusion.x11.core.XPropertyInstanceXAtoms;
import org.fusion.x11.core.XPropertyInstanceXWindow;
import org.fusion.x11.core.XPropertyInstanceXWindows;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.ewmh.EwmhAtoms;
import org.fusion.x11.ewmh.EwmhClientMessageEmitter;
import org.fusion.x11.ewmh._NetActiveWindowMessage;
import org.fusion.x11.ewmh._NetCloseWindowMessage;
import org.fusion.x11.ewmh._NetCurrentDesktopMessage;
import org.fusion.x11.ewmh._NetDesktopGeometryMessage;
import org.fusion.x11.ewmh._NetDesktopViewPortInstance;
import org.fusion.x11.ewmh._NetMoveResizeWindowMessage;
import org.fusion.x11.ewmh._NetNumberOfDesktopsMessage;
import org.fusion.x11.ewmh._NetRequestFrameExtentsMessage;
import org.fusion.x11.ewmh._NetRestackWindowMessage;
import org.fusion.x11.ewmh._NetWmFullscreenMonitorsMessage;
import org.fusion.x11.ewmh._NetWmMoveResizeMessage;
import org.fusion.x11.ewmh._NetWmPingMessage;
import org.fusion.x11.ewmh._NetWmStateMessage;
import org.fusion.x11.ewmh._NetDesktopViewPortInstance.DesktopViewPortCoordinate;
import org.fusion.x11.ewmh._NetDesktopViewPortMessage;
import org.fusion.x11.ewmh._NetShowingDesktopInstance;
import org.fusion.x11.ewmh._NetShowingDesktopMessage;
import org.fusion.x11.ewmh._NetWmDesktopMessage;
import org.fusion.x11.ewmh._NetWmSyncRequestMessage;
import org.fusion.x11.ewmh._NetWorkAreaInstance;
import org.fusion.x11.ewmh._NetWorkAreaInstance.WorkAreaGeometry;
import org.hydrogen.displayinterface.PropertyInstanceText;
import org.hydrogen.displayinterface.PropertyInstanceTexts;
import org.hydrogen.eventsystem.EventHandler;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.core.RenderAreaPropertiesManipulator;
import org.hyperdrive.core.WindowManagementInfoListener;
import org.hyperdrive.widget.RealRoot;
import org.hyperdrive.widget.VirtualRoot;

//x11 ewmh protocol manager, under development
//TODO refactor
//TODO documentation
/**
 * under development
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class EwmhManager implements WindowManagementInfoListener {

	// Logging
	private static final Logger logger = Logger.getLogger(EwmhManager.class);
	private static final String EWMH_CLIENTMESSAGE_LOGMESSAGE = "Ewmh client message received: %s";
	private static final String EWMH_PROPERTY_LOGMESSAGE = "Ewmh property updated - %s=%s";

	//
	// START EWMH CLIENT MESSAGE HANDLERS

	private class NetActiveWindowMessageHandler implements
			EventHandler<_NetActiveWindowMessage> {

		private NetActiveWindowMessageHandler() {
		}

		@Override
		public void handleEvent(_NetActiveWindowMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetCloseWindowMessageHandler implements
			EventHandler<_NetCloseWindowMessage> {
		private NetCloseWindowMessageHandler() {

		}

		@Override
		public void handleEvent(_NetCloseWindowMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetCurrentDesktopMessageHandler implements
			EventHandler<_NetCurrentDesktopMessage> {
		private NetCurrentDesktopMessageHandler() {

		}

		@Override
		public void handleEvent(_NetCurrentDesktopMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetDesktopGeometryMessageHandler implements
			EventHandler<_NetDesktopGeometryMessage> {
		private NetDesktopGeometryMessageHandler() {

		}

		@Override
		public void handleEvent(_NetDesktopGeometryMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetDesktopViewPortMessageHandler implements
			EventHandler<_NetDesktopViewPortMessage> {
		private NetDesktopViewPortMessageHandler() {

		}

		@Override
		public void handleEvent(_NetDesktopViewPortMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetMoveResizeWindowMessageHandler implements
			EventHandler<_NetMoveResizeWindowMessage> {
		private NetMoveResizeWindowMessageHandler() {

		}

		@Override
		public void handleEvent(_NetMoveResizeWindowMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetNumberOfDesktopsMessageHandler implements
			EventHandler<_NetNumberOfDesktopsMessage> {
		private NetNumberOfDesktopsMessageHandler() {

		}

		@Override
		public void handleEvent(_NetNumberOfDesktopsMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetRequestFrameExtentsMessageHandler implements
			EventHandler<_NetRequestFrameExtentsMessage> {
		private NetRequestFrameExtentsMessageHandler() {

		}

		@Override
		public void handleEvent(_NetRequestFrameExtentsMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetRestackWindowMessageHandler implements
			EventHandler<_NetRestackWindowMessage> {
		private NetRestackWindowMessageHandler() {

		}

		@Override
		public void handleEvent(_NetRestackWindowMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetShowingDesktopMessageHandler implements
			EventHandler<_NetShowingDesktopMessage> {
		private NetShowingDesktopMessageHandler() {

		}

		@Override
		public void handleEvent(_NetShowingDesktopMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetWmDesktopMessageHandler implements
			EventHandler<_NetWmDesktopMessage> {
		private NetWmDesktopMessageHandler() {

		}

		@Override
		public void handleEvent(_NetWmDesktopMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetWmFullscreenMonitorsMessageHandler implements
			EventHandler<_NetWmFullscreenMonitorsMessage> {
		private NetWmFullscreenMonitorsMessageHandler() {

		}

		@Override
		public void handleEvent(_NetWmFullscreenMonitorsMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetWmMoveResizeMessageHandler implements
			EventHandler<_NetWmMoveResizeMessage> {
		private NetWmMoveResizeMessageHandler() {

		}

		@Override
		public void handleEvent(_NetWmMoveResizeMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetWmPingMessageHandler implements
			EventHandler<_NetWmPingMessage> {
		private NetWmPingMessageHandler() {

		}

		@Override
		public void handleEvent(_NetWmPingMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetWmStateMessageHandler implements
			EventHandler<_NetWmStateMessage> {
		private NetWmStateMessageHandler() {

		}

		@Override
		public void handleEvent(_NetWmStateMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	private class NetWmSyncRequestMessageHandler implements
			EventHandler<_NetWmSyncRequestMessage> {
		private NetWmSyncRequestMessageHandler() {

		}

		@Override
		public void handleEvent(_NetWmSyncRequestMessage event) {
			// Debug
			logger.trace(String.format(EWMH_CLIENTMESSAGE_LOGMESSAGE, event));
			// TODO Auto-generated method stub

		}
	}

	// END EWMH CLIENT MESSAGE HANDLERS

	private final ManagedDisplay managedDisplay;
	private final EwmhClientMessageEmitter clientMessageEmitter;
	private final RenderAreaPropertiesManipulator realRootPropertyManipulator;
	private final XDisplay nativeDisplay;

	public EwmhManager(final ManagedDisplay managedDisplay) {
		this.managedDisplay = managedDisplay;
		nativeDisplay = (XDisplay) getManagedDisplay().getDisplay();
		final RealRoot realRoot = getManagedDisplay().getRealRootRenderArea();
		realRootPropertyManipulator = new RenderAreaPropertiesManipulator(
				realRoot);

		managedDisplay.getWindowManagementInfo()
				.addWindowManagementInfoListener(this);
		this.clientMessageEmitter = new EwmhClientMessageEmitter(
				(XDisplay) getManagedDisplay().getDisplay());

		initInitialEwmhProperties();
		initEwmhClientMessageHandlers();
	}

	private void initEwmhClientMessageHandlers() {
		clientMessageEmitter.addEventHandler(
				new NetActiveWindowMessageHandler(),
				_NetActiveWindowMessage.TYPE);
		clientMessageEmitter
				.addEventHandler(new NetCloseWindowMessageHandler(),
						_NetCloseWindowMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetCurrentDesktopMessageHandler(),
				_NetCurrentDesktopMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetDesktopGeometryMessageHandler(),
				_NetDesktopGeometryMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetDesktopViewPortMessageHandler(),
				_NetDesktopViewPortMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetMoveResizeWindowMessageHandler(),
				_NetMoveResizeWindowMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetNumberOfDesktopsMessageHandler(),
				_NetNumberOfDesktopsMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetRequestFrameExtentsMessageHandler(),
				_NetRequestFrameExtentsMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetRestackWindowMessageHandler(),
				_NetRestackWindowMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetShowingDesktopMessageHandler(),
				_NetShowingDesktopMessage.TYPE);
		clientMessageEmitter.addEventHandler(new NetWmDesktopMessageHandler(),
				_NetWmDesktopMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetWmFullscreenMonitorsMessageHandler(),
				_NetWmFullscreenMonitorsMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetWmMoveResizeMessageHandler(),
				_NetWmMoveResizeMessage.TYPE);
		clientMessageEmitter.addEventHandler(new NetWmPingMessageHandler(),
				_NetWmPingMessage.TYPE);
		clientMessageEmitter.addEventHandler(new NetWmStateMessageHandler(),
				_NetWmStateMessage.TYPE);
		clientMessageEmitter.addEventHandler(
				new NetWmSyncRequestMessageHandler(),
				_NetWmSyncRequestMessage.TYPE);
	}

	private void initInitialEwmhProperties() {
		// TODO set root window properties
		// TODO set virtual root window properties

		final RealRoot realRoot = getManagedDisplay().getRealRootRenderArea();
		final XDisplay nativeDisplay = (XDisplay) getManagedDisplay()
				.getDisplay();

		// Root Window Properties (and Related Messages)
		// _NET_SUPPORTED
		// _NET_CLIENT_LIST v
		// _NET_CLIENT_LIST_STACKING v
		// _NET_NUMBER_OF_DESKTOPS v
		// _NET_DESKTOP_GEOMETRY v
		// _NET_DESKTOP_VIEWPORT v
		// _NET_CURRENT_DESKTOP v
		// _NET_DESKTOP_NAMES v
		// _NET_ACTIVE_WINDOW v
		// _NET_WORKAREA v
		// _NET_SUPPORTING_WM_CHECK v
		// _NET_VIRTUAL_ROOTS v
		// _NET_DESKTOP_LAYOUT
		// _NET_SHOWING_DESKTOP v

		final VirtualRoot activeVirtualRoot = getManagedDisplay()
				.getWindowManagementInfo().getVirtualRootIsActive();

		final RenderAreaPropertiesManipulator realRootPropertyManipulator = new RenderAreaPropertiesManipulator(
				realRoot);

		updateNetNumberOfDesktopsProperty();

		// TODO update this when the active virtual root changes size.
		realRootPropertyManipulator.setPropertyValue(
				"_NET_DESKTOP_GEOMETRY",
				new XPropertyInstanceCardinals(nativeDisplay, activeVirtualRoot
						.getWidth(), activeVirtualRoot.getHeight()));

		updateNetCurrentDesktop();

		updateNetVirtualRoots();

		// TODO update this when moving/unmapping all clients so only the
		// desktop is visible
		realRootPropertyManipulator.setPropertyValue("_NET_SHOWING_DESKTOP",
				new _NetShowingDesktopInstance(nativeDisplay, false));

		// TODO use another invisible widget that is never destroyed
		// instead
		realRootPropertyManipulator.setPropertyValue(
				"_NET_SUPPORTING_WM_CHECK",
				new XPropertyInstanceXWindow(nativeDisplay,
						(XWindow) activeVirtualRoot.getPlatformRenderArea()));

		final RenderAreaPropertiesManipulator virtRootPropertyManipulator = new RenderAreaPropertiesManipulator(
				activeVirtualRoot);

		// TODO use another invisible widget that is never destroyed
		// instead, then set this property on that widget
		virtRootPropertyManipulator.setPropertyValue(
				"_NET_SUPPORTING_WM_CHECK",
				new XPropertyInstanceXWindow(nativeDisplay,
						(XWindow) activeVirtualRoot.getPlatformRenderArea()));

		virtRootPropertyManipulator
				.setPropertyValue("_NET_WM_NAME", new PropertyInstanceText(
						nativeDisplay, "UTF8_STRING", "TNT WM"));

		updateNetDesktopNames();

		// TODO update this when the current active virtual root's geometry
		// changes.
		realRootPropertyManipulator.setPropertyValue(
				"_NET_WORKAREA",
				new _NetWorkAreaInstance(nativeDisplay, new WorkAreaGeometry(
						activeVirtualRoot.getRelativeX(), activeVirtualRoot
								.getRelativeY(), activeVirtualRoot.getWidth(),
						activeVirtualRoot.getHeight() - 40)));

		// TODO not sure what this does.
		realRootPropertyManipulator.setPropertyValue("_NET_DESKTOP_VIEWPORT",
				new _NetDesktopViewPortInstance(nativeDisplay,
						new DesktopViewPortCoordinate(0, 0)));

		for (VirtualRoot virtRoot : getManagedDisplay()
				.getWindowManagementInfo().getVirtualRootsCreated()) {
			setVirtRootNetWmWindowType(virtRoot);
		}

		// Note that _NET_CLIENT_LIST, _NET_CLIENT_LIST_STACKING and
		// _NET_ACTIVE_WINDOW are set dynamically when a client is added or a
		// client's state changes.
		// TODO client creation list in order (_NET_CLIENT_LIST)
		// TODO client stacking list in order (_NET_CLIENT_LIST)
		// TODO virtual root order (_NET_DESKTOP_LAYOUT)
	}

	public RenderAreaPropertiesManipulator getRealRootPropertyManipulator() {
		return realRootPropertyManipulator;
	}

	public ManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	public XDisplay getNativeDisplay() {
		return nativeDisplay;
	}

	private void updateNetNumberOfDesktopsProperty() {
		int nroDesktops = getManagedDisplay().getWindowManagementInfo()
				.getVirtualRootsCreated().length;

		String atomName = EwmhAtoms.NET_NUMBER_OF_DESKTOPS_ATOM_NAME;
		XPropertyInstanceCardinal propInstance = new XPropertyInstanceCardinal(
				getNativeDisplay(), nroDesktops);

		logger.trace(String.format(EWMH_PROPERTY_LOGMESSAGE, atomName,
				propInstance));

		getRealRootPropertyManipulator().setPropertyValue(atomName,
				propInstance);
	}

	private void updateNetVirtualRoots() {
		int nroDesktops = getManagedDisplay().getWindowManagementInfo()
				.getVirtualRootsCreated().length;
		XWindow[] virtRootWindows = new XWindow[nroDesktops];
		for (int i = 0; i < nroDesktops; i++) {
			virtRootWindows[i] = (XWindow) getManagedDisplay()
					.getWindowManagementInfo().getVirtualRootsCreated()[i]
					.getPlatformRenderArea();
		}

		String atomName = EwmhAtoms.NET_VIRTUAL_ROOTS_ATOM_NAME;
		XPropertyInstanceXWindows propInstance = new XPropertyInstanceXWindows(
				nativeDisplay, virtRootWindows);

		logger.trace(String.format(EWMH_PROPERTY_LOGMESSAGE, atomName,
				propInstance));

		realRootPropertyManipulator.setPropertyValue(atomName, propInstance);
	}

	private void updateNetDesktopNames() {
		// TODO set this on every newly created virtual root. Auto increment
		// name number.
		int nroDesktops = getManagedDisplay().getWindowManagementInfo()
				.getVirtualRootsCreated().length;
		String[] desktopNames = new String[nroDesktops];
		for (int i = 0; i < nroDesktops; i++) {
			desktopNames[i] = String.format("VD%s", i);
		}

		String atomName = EwmhAtoms.NET_DESKTOP_NAMES_ATOM_NAME;
		PropertyInstanceTexts propInstance = new PropertyInstanceTexts(
				nativeDisplay, "UTF8_STRING", desktopNames);

		logger.trace(String.format(EWMH_PROPERTY_LOGMESSAGE, atomName,
				propInstance));

		realRootPropertyManipulator.setPropertyValue("_NET_DESKTOP_NAMES",
				propInstance);
	}

	private void updateNetCurrentDesktop() {
		// update this when the active virtual root switches to another
		// virtual root
		VirtualRoot activeVirtualRoot = getManagedDisplay()
				.getWindowManagementInfo().getVirtualRootIsActive();

		int i = 0;
		for (VirtualRoot virtualRoot : getManagedDisplay()
				.getWindowManagementInfo().getVirtualRootsCreated()) {
			if (virtualRoot.equals(activeVirtualRoot)) {
				break;
			}
			i++;
		}

		String atomName = EwmhAtoms.NET_CURRENT_DESKTOP_ATOM_NAME;
		XPropertyInstanceCardinal propInstance = new XPropertyInstanceCardinal(
				nativeDisplay, i);

		logger.trace(String.format(EWMH_PROPERTY_LOGMESSAGE, atomName,
				propInstance));

		realRootPropertyManipulator.setPropertyValue("_NET_CURRENT_DESKTOP",
				propInstance);
	}

	private void setVirtRootNetWmWindowType(VirtualRoot virtualRoot) {
		final RenderAreaPropertiesManipulator virtRootPropertyManipulator = new RenderAreaPropertiesManipulator(
				virtualRoot);

		String atomName = EwmhAtoms.NET_WM_WINDOW_TYPE_ATOM_NAME;
		XPropertyInstanceXAtoms propInstance = new XPropertyInstanceXAtoms(
				nativeDisplay, "_NET_WM_WINDOW_TYPE_DESKTOP");

		logger.trace(String.format(EWMH_PROPERTY_LOGMESSAGE, atomName,
				propInstance));

		virtRootPropertyManipulator.setPropertyValue(atomName, propInstance);
	}

	@Override
	public void onVirtualRootNew(final VirtualRoot virtualRoot) {
		setVirtRootNetWmWindowType(virtualRoot);
		updateNetNumberOfDesktopsProperty();
		updateNetVirtualRoots();
		updateNetDesktopNames();
	}

	@Override
	public void onVirtualRootDestroyed(final VirtualRoot virtualRoot) {
		updateNetNumberOfDesktopsProperty();
		updateNetVirtualRoots();
		updateNetDesktopNames();
	}

	@Override
	public void onVirtualRootActivate(final VirtualRoot virtualRootActive) {
		updateNetCurrentDesktop();
	}

	@Override
	public void onClientWindowGainFocus(final ClientWindow clientWindow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientWindowLostFocus(final ClientWindow clientWindow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientWindowRaise(final ClientWindow clientWindow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientWindowLower(final ClientWindow clientWindow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientWindowNew(final ClientWindow clientWindow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientWindowDestroyed(final ClientWindow clientWindow) {
		// TODO Auto-generated method stub

	}
}
