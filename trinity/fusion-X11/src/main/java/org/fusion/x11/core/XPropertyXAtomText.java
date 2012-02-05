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
import java.nio.charset.Charset;

import org.hydrogen.displayinterface.Atom;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PropertyInstance;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class XPropertyXAtomText<T extends PropertyInstance> extends
		XPropertyXAtom<T> {

	/**
	 * 
	 * @param atomName
	 * @param atomId
	 * @param xAtomRegistry
	 */
	public XPropertyXAtomText(final XDisplay display, final String atomName,
			final Long atomId) {
		super(display, atomName, atomId);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtomText(final XDisplay display, final String atomName) {
		super(display, atomName);
	}

	/**
	 * Read a single text from the entire length of the property value.
	 * 
	 * @return
	 * 
	 */
	protected String readGenericText(
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyValue) {

		// 8,16 or 32 bits
		final int format = propertyInstanceInfo.getFormat();
		final long length = propertyInstanceInfo.getLength() * (format / 8);
		final byte[] rawNameData = propertyValue.getData((int) length);

		final XAtom encodingAtom = propertyInstanceInfo.getType();
		final String text = this.byteArrayToText(encodingAtom, rawNameData);
		return text;
	}

	/**
	 * 
	 * @param buffer
	 * @return
	 */
	protected byte[] readNullTerminatedByteArray(final ByteBuffer buffer) {
		int i = 0;
		byte rawByte = buffer.get(i);
		while (rawByte != '\00') {
			i++;
			rawByte = buffer.get(i);
		}
		final byte[] rawBytes = new byte[i];
		buffer.get(rawBytes);
		return rawBytes;
	}

	private static final String STRING = "STRING";
	private static final String ISO8859_1 = "ISO8859_1";

	private static final String UTF8_TEXT = "UTF8_TEXT";
	private static final String UTF8 = "UTF8";

	private static final String ASCII = "ASCII";

	/**
	 * 
	 * @param encodingAtom
	 * @return
	 */
	protected Charset charsetFromXAtom(final XAtom encodingAtom) {
		Charset encodingCharset;
		if ((encodingAtom != null)
				&& encodingAtom.getAtomName().equals(XPropertyXAtomText.STRING)) {
			encodingCharset = Charset.forName(XPropertyXAtomText.ISO8859_1);
		} else if ((encodingAtom != null)
				&& encodingAtom.getAtomName().equals(
						XPropertyXAtomText.UTF8_TEXT)) {
			encodingCharset = Charset.forName(XPropertyXAtomText.UTF8);
		} else {
			// fallback to ASCII
			encodingCharset = Charset.forName(XPropertyXAtomText.ASCII);
		}
		return encodingCharset;
	}

	/**
	 * 
	 * @param platformRenderArea
	 * @param propertyInstanceType
	 * @param text
	 * 
	 */
	protected void setTextPropertyValue(
			final PlatformRenderArea platformRenderArea,
			final Atom propertyInstanceType, final String text) {
		final byte[] rawData = this.textToByteArray(
				(XAtom) propertyInstanceType, text);
		// int size + rawData size
		final FlexDataContainer rawDataContainer = new FlexDataContainer(
				rawData.length + 1);
		rawDataContainer.getBuffer().put(rawData);

		setRawPropertyValue(platformRenderArea, propertyInstanceType,
				rawDataContainer);
	}

	/**
	 * 
	 * @param encodingAtom
	 * @param text
	 * @return
	 */
	protected byte[] textToByteArray(final XAtom encodingAtom, final String text) {
		final Charset encodingCharset = this.charsetFromXAtom(encodingAtom);
		final byte[] data = text.getBytes(encodingCharset);
		return data;
	}

	/**
	 * 
	 * @param encodingAtom
	 * @param rawData
	 * @return
	 */
	protected String byteArrayToText(final XAtom encodingAtom,
			final byte[] rawData) {
		final Charset encodingCharset = this.charsetFromXAtom(encodingAtom);
		final String text = new String(rawData, encodingCharset);
		return text;
	}
}
