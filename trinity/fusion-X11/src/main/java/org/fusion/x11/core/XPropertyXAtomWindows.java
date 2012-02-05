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

import org.hydrogen.displayinterface.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomWindows extends
		XPropertyXAtom<XPropertyInstanceXWindows> {

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtomWindows(final XDisplay display, final String atomName) {
		super(display, atomName);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 */
	public XPropertyXAtomWindows(final XDisplay display, final String atomName,
			final Long atomId) {
		super(display, atomName, atomId);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceXWindows propertyInstance) {
		final XWindow[] windows = propertyInstance.getPlatformRenderAreas();
		final IntDataContainer rawDataContainer = new IntDataContainer(
				windows.length);
		for (final XWindow window : windows) {
			final int windowId = window.getDisplayResourceHandle()
					.getResourceHandle().getNativeHandle().intValue();
			rawDataContainer.writeDataBlock(Integer.valueOf(windowId));
		}

		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public XPropertyInstanceXWindows getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final XWindow[] windows = new XWindow[(int) getPrimitivePropertyLength(propertyInstanceInfo)];
		for (int i = 0; i < windows.length; i++) {
			final long windowId = Long.valueOf(propertyDataContainer
					.readUnsignedInt());
			final XID xid = new XID(getDisplay(),
					XResourceHandle.valueOf(windowId));
			final XWindow window = getDisplay().getDisplayPlatform()
					.getResourcesRegistry().getClientXWindow(xid, true);
			windows[i] = window;
		}

		final XPropertyInstanceXWindows reply = new XPropertyInstanceXWindows(
				getDisplay(), windows);
		return reply;
	}
}