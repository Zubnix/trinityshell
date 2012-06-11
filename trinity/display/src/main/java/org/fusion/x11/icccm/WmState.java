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

import org.fusion.x11.core.FlexDataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.trinity.core.display.api.PlatformRenderArea;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XIDImpl;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.XWindowImpl;
import org.trinity.display.x11.impl.property.XPropertyInstanceInfo;
import org.trinity.display.x11.impl.property.AbstractXProperty;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class WmState extends AbstractXProperty<WmStateInstance> {

	/**
	 * 
	 * @param display
	 * 
	 */
	public WmState(final XServerImpl display) {
		super(display, IcccmAtoms.WM_STATE_ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final WmStateInstance propertyInstance) {
		final int state = propertyInstance.getState().ordinal();
		final int iconWindowId = propertyInstance.getIconWindow()
				.getDisplayResourceHandle().getResourceHandle()
				.getNativeHandle().intValue();

		final IntDataContainer rawDataContainer = new IntDataContainer(2);
		rawDataContainer.writeDataBlock(Integer.valueOf(state));
		rawDataContainer.writeDataBlock(Integer.valueOf(iconWindowId));

		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public WmStateInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final long state = propertyDataContainer.readUnsignedInt();
		final long iconWindowId = propertyDataContainer.readUnsignedInt();

		final WmStateEnum wmState = WmStateEnum.values()[(int) state];
		final XWindowImpl iconWindow = getDisplay()
				.getDisplayPlatform()
				.getResourcesRegistry()
				.getClientXWindow(
						new XIDImpl(getDisplay(), XResourceHandleImpl.valueOf(Long
								.valueOf(iconWindowId))));

		final WmStateInstance reply = new WmStateInstance(getDisplay(),
				wmState, iconWindow);

		return reply;
	}

}
