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

import org.trinity.dispay.x11.api.extension.composite.XExtensionComposite;
import org.trinity.display.x11.api.extension.XExtensionFactory;
import org.trinity.display.x11.api.extension.damage.XExtensionDamage;
import org.trinity.display.x11.api.extension.fixes.XExtensionXFixes;
import org.trinity.display.x11.api.extension.render.XExtensionXRender;
import org.trinity.display.x11.api.extension.shape.XExtensionXShape;
import org.trinity.display.x11.api.extension.sync.XExtensionXSync;
import org.trinity.display.x11.impl.XCallerImpl;
import org.trinity.display.x11.impl.XServerImpl;

// currently unused
public class XcbExtensions implements XExtensionFactory {
	private final XcbExtensionComposite xcbExtensionComposite;
	private final XcbExtensionDamage xcbExtensionDamage;
	private final XcbExtensionXFixes xcbExtensionXFixes;
	private final XcbExtensionXRender xcbExtensionXRender;
	private final XcbExtensionXShape xcbExtensionXShape;
	private final XcbExtensionXSync xcbExtensionXSync;

	public XcbExtensions(final XServerImpl display,
			final XCallerImpl nativeCaller) {
		final XExtensionNativeCalls nativeCalls = new XExtensionNativeCalls();

		this.xcbExtensionComposite = new XcbExtensionComposite(nativeCaller,
				nativeCalls, display);
		this.xcbExtensionDamage = new XcbExtensionDamage(nativeCaller,
				nativeCalls, display);
		this.xcbExtensionXFixes = new XcbExtensionXFixes(nativeCaller,
				nativeCalls, display);
		this.xcbExtensionXRender = new XcbExtensionXRender(nativeCaller,
				nativeCalls, display);
		this.xcbExtensionXShape = new XcbExtensionXShape(nativeCaller,
				nativeCalls, display);
		this.xcbExtensionXSync = new XcbExtensionXSync(nativeCaller,
				nativeCalls, display);

	}

	@Override
	public XExtensionXShape getExtensionXShape() {
		return this.xcbExtensionXShape;
	}

	@Override
	public XExtensionXRender getXExtensionXRender() {
		return this.xcbExtensionXRender;
	}

	@Override
	public XExtensionDamage getXExtensionDamage() {
		return this.xcbExtensionDamage;
	}

	@Override
	public XExtensionComposite getXExtensionComposite() {
		return this.xcbExtensionComposite;
	}

	@Override
	public XExtensionXSync getXExtensionXSync() {
		return this.xcbExtensionXSync;
	}

	@Override
	public XExtensionXFixes getXExtensionXFixes() {
		return this.xcbExtensionXFixes;
	}
}
