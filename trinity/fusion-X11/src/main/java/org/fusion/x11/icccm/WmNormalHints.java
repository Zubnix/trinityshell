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
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceInfo;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XProtocolConstants;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.error.NotYetImplementedError;
import org.hydrogen.displayinterface.PlatformRenderArea;
 

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class WmNormalHints extends XPropertyXAtom<WmSizeHintsInstance> {

	public static final String ATOM_NAME = "WM_NORMAL_HINTS";

	/**
	 * 
	 * @param atomId
	 * @param xAtomRegistry
	 */
	public WmNormalHints(final XDisplay display) {
		super(display, IcccmAtoms.WM_NORMAL_HINTS_ATOM_NAME, Long
				.valueOf(XProtocolConstants.WM_NORMAL_HINTS));
	}

	@Override
	public WmSizeHintsInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer)
			   {
		// contents of buffer:
		// /** User specified flags */
		// uint32_t flags;
		// /** User-specified position */
		// int32_t x, y;
		// /** User-specified size */
		// int32_t width, height;
		// /** Program-specified minimum size */
		// int32_t min_width, min_height;
		// /** Program-specified maximum size */
		// int32_t max_width, max_height;
		// /** Program-specified resize increments */
		// int32_t width_inc, height_inc;
		// /** Program-specified minimum aspect ratios */
		// int32_t min_aspect_num, min_aspect_den;
		// /** Program-specified maximum aspect ratios */
		// int32_t max_aspect_num, max_aspect_den;
		// /** Program-specified base size */
		// int32_t base_width, base_height;
		// /** Program-specified window gravity */
		// uint32_t win_gravity;

		final long flags = propertyDataContainer.readUnsignedInt();
		final int x = propertyDataContainer.readSignedInt();
		final int y = propertyDataContainer.readSignedInt();
		final int width = propertyDataContainer.readSignedInt();
		final int height = propertyDataContainer.readSignedInt();
		final int minWidth = propertyDataContainer.readSignedInt();
		final int minHeight = propertyDataContainer.readSignedInt();
		final int maxWidth = propertyDataContainer.readSignedInt();
		final int maxHeight = propertyDataContainer.readSignedInt();
		final int widthInc = propertyDataContainer.readSignedInt();
		final int heightInc = propertyDataContainer.readSignedInt();
		final int minAspectNum = propertyDataContainer.readSignedInt();
		final int minAspectDen = propertyDataContainer.readSignedInt();
		final int maxAspectNum = propertyDataContainer.readSignedInt();
		final int maxAspectDen = propertyDataContainer.readSignedInt();
		final int baseWidth = propertyDataContainer.readSignedInt();
		final int baseHeight = propertyDataContainer.readSignedInt();
		final long winGravity = propertyDataContainer.readSignedInt();

		// f*ck*ng X borders...
		final int borderWidth = ((XWindow) platformRenderArea)
				.getPlatformRenderAreaGeometry().getBorderWidth();

		return new WmSizeHintsInstance(getDisplay(), flags, x, y, width
				+ borderWidth, height + borderWidth, minWidth + borderWidth,
				minHeight + borderWidth, maxWidth + borderWidth, maxHeight
						+ borderWidth, widthInc, heightInc, minAspectNum,
				minAspectDen, maxAspectNum, maxAspectDen, baseWidth,
				baseHeight, winGravity);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final WmSizeHintsInstance propertyInstance)
			   {
		throw new NotYetImplementedError("Not implemented");
	}
}
