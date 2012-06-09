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
import org.trinity.core.display.api.PlatformRenderArea;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.property.XPropertyInstanceInfo;
import org.trinity.display.x11.impl.property.XPropertyXAtom;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetDesktopLayout extends
		XPropertyXAtom<_NetDesktopLayoutInstance> {
	// CARDINAL[4]/32
	// orientation, columns, rows, starting_corner

	/**
	 * 
	 * @param display
	 * 
	 */
	public _NetDesktopLayout(final XServerImpl display) {
		super(display, EwmhAtoms.NET_DESKTOP_LAYOUT_ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final _NetDesktopLayoutInstance propertyInstance) {
		final int orientation = propertyInstance.getOrientation().ordinal();
		final int columns = propertyInstance.getColumns();
		final int rows = propertyInstance.getRows();
		final int startingCorner = propertyInstance.getStartingCorner()
				.ordinal();

		final IntDataContainer rawDataContainer = new IntDataContainer(4);
		rawDataContainer.writeDataBlock(Integer.valueOf(orientation));
		rawDataContainer.writeDataBlock(Integer.valueOf(columns));
		rawDataContainer.writeDataBlock(Integer.valueOf(rows));
		rawDataContainer.writeDataBlock(Integer.valueOf(startingCorner));

		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public _NetDesktopLayoutInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final int orientation = (int) propertyDataContainer.readUnsignedInt();
		final int columns = (int) propertyDataContainer.readUnsignedInt();
		final int rows = (int) propertyDataContainer.readUnsignedInt();
		final int startingCorner = (int) propertyDataContainer
				.readUnsignedInt();

		final DesktopLayoutOrientation layoutOrientation = DesktopLayoutOrientation
				.values()[orientation];
		final DesktopLayoutStartingPoint startingPoint = DesktopLayoutStartingPoint
				.values()[startingCorner];

		final _NetDesktopLayoutInstance reply = new _NetDesktopLayoutInstance(
				getDisplay(), layoutOrientation, columns, rows, startingPoint);
		return reply;
	}
}
