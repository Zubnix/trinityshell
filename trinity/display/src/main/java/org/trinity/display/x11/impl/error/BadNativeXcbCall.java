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
package org.trinity.display.x11.impl.error;

import java.util.Date;


// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BadNativeXcbCall extends BadNativeXCall {

	/**
     * 
     */
	private static final long serialVersionUID = -417883902036303471L;

	private final XErrorCode xErrorCode;
	private final long resourceId;
	private final Date timestamp;

	/**
	 * 
	 * @param errorCode
	 * @param resourceId
	 * @param timestamp
	 */
	public BadNativeXcbCall(final XErrorCode errorCode, final long resourceId,
			final Date timestamp) {
		this.xErrorCode = errorCode;
		this.resourceId = resourceId;
		this.timestamp = timestamp;
	}

	/**
	 * 
	 * @return
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * 
	 * @return
	 */
	public long getResourceId() {
		return this.resourceId;
	}

	@Override
	public String toString() {
		return String.format("Native error :: %s - resource id :: %d",
				getxErrorCode(), getResourceId());
	}

	/**
	 * 
	 * @return
	 */
	public XErrorCode getxErrorCode() {
		return this.xErrorCode;
	}
}
