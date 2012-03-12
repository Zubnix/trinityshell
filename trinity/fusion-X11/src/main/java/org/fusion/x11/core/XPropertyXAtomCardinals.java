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

import org.hydrogen.api.display.PlatformRenderArea;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomCardinals extends
		XPropertyXAtom<XPropertyInstanceCardinals> {

	private final int nroCardinals;

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 * @param nroCardinals
	 */
	public XPropertyXAtomCardinals(final XDisplay display,
			final String atomName, final Long atomId, final int nroCardinals) {
		super(display, atomName, atomId);
		this.nroCardinals = nroCardinals;
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param nroCardinals
	 * 
	 */
	public XPropertyXAtomCardinals(final XDisplay display,
			final String atomName, final int nroCardinals) {
		super(display, atomName);
		this.nroCardinals = nroCardinals;
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceCardinals propertyInstance) {
		final IntDataContainer rawDataContainer = new IntDataContainer(
				this.nroCardinals);
		for (int i = 0; i < this.nroCardinals; i++) {
			rawDataContainer.writeDataBlock(Integer.valueOf(propertyInstance
					.getNumbers()[i]));
		}
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public XPropertyInstanceCardinals getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final int[] numbers = new int[this.nroCardinals];

		for (int i = 0; i < this.nroCardinals; i++) {
			numbers[0] = (int) (propertyInstanceInfo.getLength() == 0 ? 0
					: propertyDataContainer.readUnsignedInt());
		}

		final XPropertyInstanceCardinals reply = new XPropertyInstanceCardinals(
				getDisplay(), numbers);
		return reply;
	}
}