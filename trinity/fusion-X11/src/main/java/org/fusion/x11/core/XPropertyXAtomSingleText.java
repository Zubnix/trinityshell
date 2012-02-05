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

import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PropertyInstanceText;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomSingleText extends
		XPropertyXAtomText<PropertyInstanceText> {

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 */
	public XPropertyXAtomSingleText(final XDisplay display,
			final String atomName, final Long atomId) {
		super(display, atomName, atomId);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtomSingleText(final XDisplay display,
			final String atomName) {
		super(display, atomName);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final PropertyInstanceText propertyInstance) {
		final String text = propertyInstance.getText();
		final byte[] rawText = textToByteArray(
				(XAtom) propertyInstance.getType(), text);
		final ByteDataContainer rawDataContainer = new ByteDataContainer(
				rawText);
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public PropertyInstanceText getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final String text = readGenericText(propertyInstanceInfo,
				propertyDataContainer);
		return new PropertyInstanceText(propertyInstanceInfo.getType(), text);
	}
}
