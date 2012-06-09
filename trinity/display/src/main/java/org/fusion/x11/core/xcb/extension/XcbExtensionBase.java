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
package org.fusion.x11.core.xcb.extension;

import org.trinity.display.x11.impl.XCallerImpl;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.extension.XExtensionImpl;
import org.trinity.display.x11.impl.xcb.AbstractXcbCall;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbExtensionBase extends XExtensionImpl {

	private static final String UNSUPPORTE_EXTENSION_WARNING = "WARNING: X extension %s v%d.%d is not supported. Subsequent calls to this extension will fail!";

	protected final XExtensionNativeCalls nativeCalls;
	protected final XCallerImpl nativeCaller;

	private final AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> versionReplyCall;

	private final boolean supported;

	public XcbExtensionBase(
			final XCallerImpl nativeCaller,
			final XExtensionNativeCalls nativeCalls,
			final AbstractXcbCall<XcbExtensionVersionReply, Long, Integer> versionReplyCall,
			final XServerImpl display, final String extensionName,
			final int majorVersion, final int minorVersion) {
		super(extensionName, majorVersion, minorVersion);
		this.nativeCaller = nativeCaller;
		this.nativeCalls = nativeCalls;
		this.versionReplyCall = versionReplyCall;

		supported = queryExtension(display);
		if (!isSupported()) {
			// TODO logging
			System.err.println(String.format(UNSUPPORTE_EXTENSION_WARNING,
					getName(), getMajorVersion(), getMinorVersion()));
		}
	}

	public boolean isSupported() {
		return supported;
	}

	@Override
	public boolean queryExtension(final XServerImpl display) {

		final Long displayPeer = display.getNativePeer();
		final Integer requiredMajorVersion = Integer.valueOf(getMajorVersion());
		final Integer requiredMinorVersion = Integer.valueOf(getMinorVersion());

		XcbExtensionVersionReply reply;
		reply = this.nativeCaller.doCall(this.versionReplyCall,
				displayPeer, requiredMajorVersion, requiredMinorVersion);

		return (reply.getSupportedMajorVersion() > getMajorVersion())
				|| ((reply.getSupportedMajorVersion() == getMajorVersion()) && (reply
						.getSupportedMinorVersion() >= getMinorVersion()));
	}
}
