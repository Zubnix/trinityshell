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
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XPropertyInstanceInfo;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XResourceHandle;
import org.fusion.x11.core.XWindow;
import org.hydrogen.displayinterface.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class WmState extends XPropertyXAtom<WmStateInstance> {

	/**
	 * 
	 * @param display
	 * 
	 */
	public WmState(final XDisplay display) {
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
		final XWindow iconWindow = getDisplay()
				.getDisplayPlatform()
				.getResourcesRegistry()
				.getClientXWindow(
						new XID(getDisplay(), XResourceHandle.valueOf(Long
								.valueOf(iconWindowId))), false);

		final WmStateInstance reply = new WmStateInstance(getDisplay(),
				wmState, iconWindow);

		return reply;
	}

}
