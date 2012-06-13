/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.trinity.display.x11.impl.property;

import java.nio.ByteBuffer;

import org.fusion.x11.core.DataContainer;
import org.fusion.x11.core.FlexDataContainer;
import org.trinity.display.x11.api.XAtomFactory;
import org.trinity.display.x11.api.XCall;
import org.trinity.display.x11.api.XCaller;
import org.trinity.display.x11.api.XConnection;
import org.trinity.display.x11.impl.XAtomImpl;
import org.trinity.display.x11.impl.XWindowImpl;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.PlatformRenderArea;
import org.trinity.foundation.display.api.property.Atom;
import org.trinity.foundation.display.api.property.Property;
import org.trinity.foundation.display.api.property.PropertyInstance;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 * @param <T>
 */
public abstract class AbstractXProperty<T> extends XAtomImpl implements
		Property<PropertyInstance<T>> {

	private final XConnection<Long> xConnection;
	private final XCaller xCaller;
	private final XCall<NativeBufferHelper, Long, Integer> getPropertyValue;
	private final XCall<Void, Long, Object> setPropertyValue;
	private final XAtomFactory xAtomFactory;

	public AbstractXProperty(	final XConnection<Long> xConnection,
								final XCaller xCaller,
								final XCall<NativeBufferHelper, Long, Integer> getPropertyValue,
								final XCall<Void, Long, Object> setPropertyValue,
								final XAtomFactory xAtomFactory,
								final String atomName,
								final int atomId) {
		super(atomId, atomName);
		this.xConnection = xConnection;
		this.xCaller = xCaller;
		this.getPropertyValue = getPropertyValue;
		this.setPropertyValue = setPropertyValue;
		this.xAtomFactory = xAtomFactory;

	}

	/**
	 * @param window
	 * @param rawDataContainer
	 */
	protected FlexByteContainer getRawPropertyValue(final XWindowImpl window) {
		final Long xConnectionReference = this.xConnection
				.getConnectionReference();
		final Integer windowId = window.getResourceHandle().getNativeHandle();

		final NativeBufferHelper rawPropertyBuffer = this.xCaller
				.doCall(this.getPropertyValue,
						xConnectionReference,
						windowId,
						Integer.valueOf(getAtomId()));

		final byte[] rawContents = rawPropertyBuffer.getAllData();
		rawPropertyBuffer.doneReading();

		final FlexByteContainer rawPropertyValueContainer = new FlexByteContainer(ByteBuffer
				.wrap(rawContents));

		return rawPropertyValueContainer;
	}

	protected XPropertyInstanceInfo readXAtomMetaInfo(final FlexByteContainer rawPropertyValue) {
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
		final int propertyInstanceType = (int) rawPropertyValue
				.readUnsignedInt();// type
		rawPropertyValue.readUnsignedInt();// bytes_after
		final long valueLength = rawPropertyValue.readUnsignedInt();// value_len
		rawPropertyValue.getBuffer().get(new byte[12]);// pad

		final Atom type = this.xAtomFactory.createAtom(propertyInstanceType);

		return new XPropertyInstanceInfo(format, valueLength, type);
	}

	/**
	 * @param window
	 * @param propertyInstanceType
	 * @param rawDataContainer
	 */
	protected void setRawPropertyValue(	final XWindowImpl window,
										final Atom propertyInstanceType,
										final DataContainer<?> rawDataContainer) {

		final Long connectionReference = this.xConnection
				.getConnectionReference();
		final Integer windowId = window.getResourceHandle().getNativeHandle();
		final int propertyAtomId = getAtomId();
		final int typeAtomId = propertyInstanceType.getAtomId();
		final int format = rawDataContainer.getDataFormat().getFormat();
		final byte[] data = rawDataContainer.getAllData();

		this.xCaller.doCall(this.setPropertyValue,
							connectionReference,
							Integer.valueOf(windowId),
							Integer.valueOf(propertyAtomId),
							Integer.valueOf(typeAtomId),
							format,
							data);
	}

	/**
	 * @param propertyInstanceInfo
	 * @return
	 */
	protected long getPrimitivePropertyLength(final XPropertyInstanceInfo propertyInstanceInfo) {
		return (propertyInstanceInfo.getLength() * propertyInstanceInfo
				.getFormat()) / 8;
	}

	@Override
	public PropertyInstance<T> getPropertyInstance(final PlatformRenderArea platformRenderArea) {
		final FlexByteContainer rawDataContainer = getRawPropertyValue((XWindowImpl) platformRenderArea);
		final XPropertyInstanceInfo propertyInstanceInfo = readXAtomMetaInfo(rawDataContainer);

		return this.getPropertyInstance(platformRenderArea,
										propertyInstanceInfo,
										rawDataContainer);
	}

	/**
	 * @param platformRenderArea
	 * @param propertyInstanceInfo
	 * @param propertyDataContainer
	 * @return
	 */
	protected abstract PropertyInstance<T> getPropertyInstance(	PlatformRenderArea platformRenderArea,
																XPropertyInstanceInfo propertyInstanceInfo,
																FlexByteContainer propertyDataContainer);

}
