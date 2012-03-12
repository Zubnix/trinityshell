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

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.display.PropertyInstanceTexts;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomMultiText extends
		XPropertyXAtomText<PropertyInstanceTexts> {

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 */
	public XPropertyXAtomMultiText(final XDisplay display,
			final String atomName, final Long atomId) {
		super(display, atomName, atomId);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtomMultiText(final XDisplay display, final String atomName) {
		super(display, atomName);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final PropertyInstanceTexts propertyInstance) {
		final String[] desktopNames = propertyInstance.getTexts();
		final XAtom encodingAtom = (XAtom) propertyInstance.getType();

		final List<byte[]> desktopNameByteArrays = new LinkedList<byte[]>();

		int totalSize = 0;
		for (final String text : desktopNames) {
			final byte[] desktopNameByteArray = textToByteArray(encodingAtom,
					text);
			// +null byte
			totalSize += desktopNameByteArray.length + 1;
			desktopNameByteArrays.add(desktopNameByteArray);
		}

		final FlexDataContainer rawDataContainer = new FlexDataContainer(
				totalSize);
		for (final byte[] desktopNameByteArray : desktopNameByteArrays) {
			rawDataContainer.writeRawData(desktopNameByteArray);
			// null byte termination
			rawDataContainer.writeDataBlock(Byte.valueOf((byte) 0));
		}

		setRawPropertyValue(platformRenderArea, encodingAtom, rawDataContainer);
	}

	@Override
	public PropertyInstanceTexts getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final ByteBuffer buffer = propertyDataContainer.getBuffer();

		final long propertyLength = propertyInstanceInfo.getLength();

		int bytesRead = 0;

		final List<String> texts = new LinkedList<String>();

		while (propertyLength > bytesRead) {
			final byte[] textAsBytes = readNullTerminatedByteArray(buffer);
			bytesRead += textAsBytes.length;
			final String text = byteArrayToText(getDisplay().getxCoreAtoms()
					.getUtf8String(), textAsBytes);
			texts.add(text);
		}

		final String[] returnTexts = texts.toArray(new String[texts.size()]);

		final PropertyInstanceTexts reply = new PropertyInstanceTexts(
				propertyInstanceInfo.getType(), returnTexts);
		return reply;
	}
}