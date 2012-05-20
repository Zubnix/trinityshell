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

import org.fusion.x11.core.XDrawable;
import org.fusion.x11.core.XRectangle;
import org.fusion.x11.core.extension.XDamage;
import org.fusion.x11.core.extension.XDamageNotify;
import org.hydrogen.display.api.event.base.BaseDisplayEvent;

// currently unused
public class XcbXDamageNotify extends BaseDisplayEvent implements XDamageNotify {

	private final XDamage    damage;
	private final boolean    more;
	private final XRectangle area;
	private final XRectangle geometry;

	public XcbXDamageNotify(final XDrawable eventSource,
	                        final XDamage damage,
	                        final boolean more,
	                        final XRectangle area,
	                        final XRectangle geometry) {
		super(XDamageNotify.TYPE,
		      eventSource);
		this.damage = damage;
		this.more = more;
		this.area = area;
		this.geometry = geometry;
	}

	@Override
	public XDamage getDamage() {
		return this.damage;
	}

	@Override
	public boolean isMore() {
		return this.more;
	}

	@Override
	public XRectangle getArea() {
		return this.area;
	}

	@Override
	public XRectangle getGeometry() {
		return this.geometry;
	}

	@Override
	public XDrawable getEventSource() {
		return (XDrawable) super.getEventSource();
	}
}
