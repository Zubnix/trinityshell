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

import org.fusion.x11.core.FlexDataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceInfo;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.ewmh._NetDesktopViewPortInstance.DesktopViewPortCoordinate;
import org.hydrogen.displayinterface.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetDesktopViewPort extends
		XPropertyXAtom<_NetDesktopViewPortInstance> {
	// CARDINAL[][2]/32

	/**
	 * 
	 * @param display
	 * 
	 */
	public _NetDesktopViewPort(final XDisplay display) {
		super(display, EwmhAtoms.NET_DESKTOP_VIEWPORT_ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final _NetDesktopViewPortInstance propertyInstance) {

		final DesktopViewPortCoordinate[] desktopViewPortCoordinates = propertyInstance
				.getCoordinates();
		final IntDataContainer rawDataContainer = new IntDataContainer(
				desktopViewPortCoordinates.length * 2);
		for (final DesktopViewPortCoordinate desktopViewPortCoordinate : desktopViewPortCoordinates) {
			final int x = desktopViewPortCoordinate.getX();
			final int y = desktopViewPortCoordinate.getY();
			rawDataContainer.writeDataBlock(Integer.valueOf(x));
			rawDataContainer.writeDataBlock(Integer.valueOf(y));
		}

		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public _NetDesktopViewPortInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {

		final long length = propertyInstanceInfo.getLength();
		// each viewport consists of 2 ints, which is 8 bytes total;
		final int nroViewPorts = (int) (length / 8);

		final DesktopViewPortCoordinate[] desktopViewPortCoordinates = new DesktopViewPortCoordinate[nroViewPorts];

		for (int i = 0; i < desktopViewPortCoordinates.length; i++) {
			final int x = (int) propertyDataContainer.readUnsignedInt();
			final int y = (int) propertyDataContainer.readUnsignedInt();
			final DesktopViewPortCoordinate desktopViewPortCoordinate = new DesktopViewPortCoordinate(
					x, y);
			desktopViewPortCoordinates[i] = desktopViewPortCoordinate;
		}

		final _NetDesktopViewPortInstance reply = new _NetDesktopViewPortInstance(
				getDisplay(), desktopViewPortCoordinates);

		return reply;
	}
}
