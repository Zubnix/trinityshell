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
import org.hydrogen.display.api.PlatformRenderArea;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class Version extends XPropertyXAtom<VersionInstance> {

	/**
	 * 
	 * @param display
	 * @param atomName
	 */
	public Version(final XDisplay display, final String atomName) {
		super(display, atomName);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final VersionInstance propertyInstance) {
		final IntDataContainer rawDataContainer = new IntDataContainer(2);
		for (int i = 0; i < 2; i++) {
			rawDataContainer.writeDataBlock(Integer.valueOf(propertyInstance
					.getNumbers()[i]));
		}
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);

	}

	@Override
	public VersionInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final int[] numbers = new int[2];

		for (int i = 0; i < 2; i++) {
			numbers[0] = (int) (propertyInstanceInfo.getLength() == 0 ? 0
					: propertyDataContainer.readUnsignedInt());
		}

		final VersionInstance reply = new VersionInstance(getDisplay(), numbers);
		return reply;
	}

}
