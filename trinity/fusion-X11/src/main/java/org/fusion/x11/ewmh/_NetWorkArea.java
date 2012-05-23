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
import org.fusion.x11.ewmh._NetWorkAreaInstance.WorkAreaGeometry;
import org.trinity.core.display.api.PlatformRenderArea;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWorkArea extends XPropertyXAtom<_NetWorkAreaInstance> {

	/**
	 * 
	 * @param display
	 * 
	 */
	public _NetWorkArea(final XDisplay display) {
		super(display, EwmhAtoms.NET_WORKAREA_ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final _NetWorkAreaInstance propertyInstance) {

		final WorkAreaGeometry[] workAreaGeometries = propertyInstance
				.getWorkAreaGeometries();
		final IntDataContainer rawDataContainer = new IntDataContainer(
				workAreaGeometries.length * 4);
		for (final WorkAreaGeometry workAreaGeometry : workAreaGeometries) {
			final int x = workAreaGeometry.getX();
			final int y = workAreaGeometry.getY();
			final int width = workAreaGeometry.getWidth();
			final int height = workAreaGeometry.getHeight();
			rawDataContainer.writeDataBlock(Integer.valueOf(x));
			rawDataContainer.writeDataBlock(Integer.valueOf(y));
			rawDataContainer.writeDataBlock(Integer.valueOf(width));
			rawDataContainer.writeDataBlock(Integer.valueOf(height));
		}

		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);

	}

	@Override
	public _NetWorkAreaInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final long length = propertyInstanceInfo.getLength();
		// each workarea consists of 4 ints, which is 4*4 bytes total;
		final int nroViewPorts = (int) ((length / 4) * 4);

		final WorkAreaGeometry[] workAreaGeometries = new WorkAreaGeometry[nroViewPorts];

		for (int i = 0; i < workAreaGeometries.length; i++) {
			final int x = (int) propertyDataContainer.readUnsignedInt();
			final int y = (int) propertyDataContainer.readUnsignedInt();
			final int width = (int) propertyDataContainer.readUnsignedInt();
			final int height = (int) propertyDataContainer.readUnsignedInt();
			final WorkAreaGeometry workAreaGeometry = new WorkAreaGeometry(x,
					y, width, height);
			workAreaGeometries[i] = workAreaGeometry;
		}

		final _NetWorkAreaInstance reply = new _NetWorkAreaInstance(
				getDisplay(), workAreaGeometries);

		return reply;
	}

}
