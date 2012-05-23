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
import org.fusion.x11.core.XPropertyInstanceInfo;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XProtocolConstants;
import org.trinity.core.display.api.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class WmIconSize extends XPropertyXAtom<WmIconSizeInstance> {

	/**
	 * 
	 * @param display
	 * 
	 */
	public WmIconSize(final XDisplay display) {
		super(display, IcccmAtoms.WM_ICON_SIZE_ATOM_NAME, Long
				.valueOf(XProtocolConstants.WM_ICON_SIZE));
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final WmIconSizeInstance propertyInstance) {

		final int maxWidth = propertyInstance.getMaxWidth();
		final int maxHeight = propertyInstance.getMaxHeight();
		final int widthInc = propertyInstance.getWidthInc();
		final int heightInc = propertyInstance.getHeightInc();

		final IntDataContainer rawDataContainer = new IntDataContainer(4);
		rawDataContainer.writeDataBlock(Integer.valueOf(maxWidth));
		rawDataContainer.writeDataBlock(Integer.valueOf(maxHeight));
		rawDataContainer.writeDataBlock(Integer.valueOf(widthInc));
		rawDataContainer.writeDataBlock(Integer.valueOf(heightInc));

		// contents of buffer
		// max_width CARD32
		// max_height CARD32
		// width_inc CARD32
		// height_inc CARD32
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public WmIconSizeInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {

		final int maxWidth = (int) propertyDataContainer.readUnsignedInt();
		final int maxHeight = (int) propertyDataContainer.readUnsignedInt();
		final int widthInc = (int) propertyDataContainer.readUnsignedInt();
		final int heightInc = (int) propertyDataContainer.readUnsignedInt();

		final WmIconSizeInstance reply = new WmIconSizeInstance(getDisplay(),
				maxWidth, maxHeight, widthInc, heightInc);

		return reply;
	}
}
