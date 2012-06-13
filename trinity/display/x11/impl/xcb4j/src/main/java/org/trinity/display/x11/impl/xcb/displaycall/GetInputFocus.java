/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.xcb.displaycall;

import org.trinity.display.x11.api.core.XResourceHandle;
import org.trinity.display.x11.api.core.XResourceHandleFactory;
import org.trinity.display.x11.impl.xcb.AbstractXcbCall;
import org.trinity.display.x11.impl.xcb.jni.Xcb4J;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * args: (Void) NONE
 * <p>
 * return: (Long) focus window id
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Singleton
public final class GetInputFocus extends
		AbstractXcbCall<XResourceHandle, Long, Void> {

	private final XResourceHandleFactory xResourceHandleFactory;

	@Inject
	public GetInputFocus(final XResourceHandleFactory xResourceHandleFactory) {
		this.xResourceHandleFactory = xResourceHandleFactory;
	}

	@Override
	public XResourceHandle getResult() {
		final Integer windowId = Integer.valueOf((int) getNativeBufferHelper()
				.readUnsignedInt());
		return this.xResourceHandleFactory.createResourceHandle(windowId);
	}

	@Override
	protected boolean callImpl() {
		return Xcb4J
				.nativeGetInputFocus(	getConnectionReference().longValue(),
										getNativeBufferHelper().getBuffer());
	}
}