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

import org.trinity.display.x11.impl.FlexDataContainer;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.property.XPropertyInstanceInfo;
import org.trinity.display.x11.impl.property.AbstractXProperty;
import org.trinity.foundation.display.api.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWmSyncRequestCounter extends
		AbstractXProperty<_NetWmSyncRequestCounterInstance> {

	/**
	 * 
	 * @param display
	 * 
	 */
	public _NetWmSyncRequestCounter(final XServerImpl display) {
		super(display, EwmhAtoms.NET_WM_SYNC_REQUEST_COUNTER_ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final _NetWmSyncRequestCounterInstance propertyInstance) {
		final long counter = propertyInstance.getCounter();

		final FlexDataContainer rawDataContainer = new FlexDataContainer(8);
		rawDataContainer.writeDataBlock(Long.valueOf(counter));
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public _NetWmSyncRequestCounterInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final long counter = propertyDataContainer.readSignedLong();
		final _NetWmSyncRequestCounterInstance reply = new _NetWmSyncRequestCounterInstance(
				getDisplay(), counter);
		return reply;
	}
}
