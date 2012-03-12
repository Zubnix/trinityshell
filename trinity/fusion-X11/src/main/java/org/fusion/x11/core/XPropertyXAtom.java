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

import org.hydrogen.api.display.Atom;
import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.Property;
import org.hydrogen.api.display.PropertyInstance;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @param <T>
 */
public abstract class XPropertyXAtom<T extends PropertyInstance> extends XAtom
		implements Property<T> {

	/**
	 * 
	 * @param display
	 * @param ATOM_NAME
	 * @param atomId
	 */
	public XPropertyXAtom(final XDisplay display, final String ATOM_NAME,
			final Long atomId) {
		super(display, ATOM_NAME, atomId);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtom(final XDisplay display, final String atomName) {
		super(display, atomName);
	}

	/**
	 * 
	 * @param window
	 * @param rawDataContainer
	 * 
	 */
	protected FlexDataContainer getRawPropertyValue(final XWindow window) {
		return getDisplay().getXCoreInterface().getPropertyValue(window, this);
	}

	protected XPropertyInstanceInfo readXAtomMetaInfo(
			final FlexDataContainer rawPropertyValue) {
		// uint8_t response_type; /**< */
		// uint8_t format; /**< */
		// uint16_t sequence; /**< */
		// uint32_t length; /**< */
		// xcb_atom_t type; /**< */
		// uint32_t bytes_after; /**< */
		// uint32_t value_len; /**< */
		// uint8_t pad0[12]; /**< */
		// final short respT = rawPropertyValue.readUnsignedByte();//
		// response_type
		// rawPropertyValue.getBuffer();

		rawPropertyValue.readUnsignedByte();// response_type
		final int format = rawPropertyValue.readUnsignedByte();// format
		rawPropertyValue.readUnsignedShort();// sequence
		rawPropertyValue.readUnsignedInt();// length
		final long propertyInstanceType = rawPropertyValue.readSignedInt();// type
		rawPropertyValue.readUnsignedInt();// bytes_after
		final long valueLength = rawPropertyValue.readUnsignedInt();// value_len
		rawPropertyValue.getBuffer().get(new byte[12]);// pad

		final XAtom type = getDisplay().getDisplayAtoms().getById(
				Long.valueOf(propertyInstanceType));

		return new XPropertyInstanceInfo(format, valueLength, type);
	}

	/**
	 * 
	 * @param window
	 * @param propertyInstanceType
	 * @param rawDataContainer
	 * 
	 */
	protected void setRawPropertyValue(final PlatformRenderArea window,
			final Atom propertyInstanceType,
			final DataContainer<?> rawDataContainer) {

		getDisplay().getXCoreInterface().setPropertyInstance((XWindow) window,
				this, (XAtom) propertyInstanceType, rawDataContainer);
	}

	/**
	 * 
	 * @param propertyInstanceInfo
	 * @return
	 */
	protected long getPrimitivePropertyLength(
			final XPropertyInstanceInfo propertyInstanceInfo) {
		return (propertyInstanceInfo.getLength() * propertyInstanceInfo
				.getFormat()) / 8;
	}

	@Override
	public T getPropertyInstance(final PlatformRenderArea platformRenderArea) {
		final FlexDataContainer rawDataContainer = this
				.getRawPropertyValue((XWindow) platformRenderArea);
		final XPropertyInstanceInfo propertyInstanceInfo = this
				.readXAtomMetaInfo(rawDataContainer);

		return this.getPropertyInstance(platformRenderArea,
				propertyInstanceInfo, rawDataContainer);
	}

	/**
	 * 
	 * @param platformRenderArea
	 * @param propertyInstanceInfo
	 * @param propertyDataContainer
	 * @return
	 * 
	 */
	public abstract T getPropertyInstance(
			PlatformRenderArea platformRenderArea,
			XPropertyInstanceInfo propertyInstanceInfo,
			FlexDataContainer propertyDataContainer);

}
