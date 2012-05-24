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

import org.trinity.core.display.api.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomCardinal extends
		XPropertyXAtom<XPropertyInstanceCardinal> {

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtomCardinal(final XDisplay display, final String atomName) {
		super(display, atomName);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 */
	public XPropertyXAtomCardinal(final XDisplay display,
			final String atomName, final Long atomId) {
		super(display, atomName, atomId);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceCardinal propertyInstance) {
		final IntDataContainer rawDataContainer = new IntDataContainer(1);
		rawDataContainer.writeDataBlock(Integer.valueOf(propertyInstance
				.getNumber()));
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public XPropertyInstanceCardinal getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final long number = propertyInstanceInfo.getLength() == 0 ? 0
				: propertyDataContainer.readUnsignedInt();
		final XPropertyInstanceCardinal reply = new XPropertyInstanceCardinal(
				getDisplay(), (int) number);
		return reply;
	}
}
