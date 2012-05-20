/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.display.api.event.base;

import java.util.Arrays;

import org.hydrogen.display.api.Atom;
import org.hydrogen.display.api.event.ClientMessageEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.DisplayEventType;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>BaseClientMessageEvent</code> is a basic implementation of a
 * <code>ClientMessageEvent</code>. Classes wishing to implement
 * <code>ClientMessageEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseClientMessageEvent extends BaseDisplayEvent implements
		ClientMessageEvent {

	private final Atom dataType;
	private final int dataFormat;
	private final byte[] data;

	/**
	 * @param platformRenderAreaArgument
	 * @param dataType
	 * @param dataFormat
	 * @param data
	 */
	@Inject
	protected BaseClientMessageEvent(	@Named("ClientMessage") DisplayEventType displayEventType,
										@Assisted final DisplayEventSource platformRenderAreaArgument,
										@Assisted final Atom dataType,
										@Assisted final int dataFormat,
										@Assisted final byte[] data) {
		super(displayEventType, platformRenderAreaArgument);
		this.dataType = dataType;
		this.dataFormat = dataFormat;
		this.data = data;
	}

	@Override
	public byte[] getData() {
		// return a copy so manipulation of the returned instance can take
		// place without interfering with the source.
		return Arrays.copyOf(this.data, this.data.length);
	}

	@Override
	public Atom getMessageType() {
		return this.dataType;
	}

	@Override
	public int getDataFormat() {
		return this.dataFormat;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String
				.format("%s\tDetails: message type: %s - data format: %d - raw data: %s",
						super.toString(),
						getMessageType().getAtomName(),
						getDataFormat(),
						getData());
	}
}
