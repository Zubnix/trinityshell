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
package org.fusion.x11.ewmh;

import org.fusion.x11.core.FlexDataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceInfo;
import org.fusion.x11.core.XPropertyXAtom;
import org.hydrogen.displayinterface.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetShowingDesktop extends
		XPropertyXAtom<_NetShowingDesktopInstance> {
	// CARDINAL/32

	/**
	 * 
	 * @param display
	 * 
	 */
	public _NetShowingDesktop(final XDisplay display) {
		super(display, EwmhAtoms.NET_SHOWING_DESKTOP_ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final _NetShowingDesktopInstance propertyInstance) {
		final boolean showingDesktop = propertyInstance.isShowingDesktop();
		final IntDataContainer rawDataContainer = new IntDataContainer(1);
		rawDataContainer
				.writeDataBlock(Integer.valueOf(showingDesktop ? 1 : 0));
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public _NetShowingDesktopInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {

		final int showingDesktop = (int) propertyDataContainer
				.readUnsignedInt();
		final _NetShowingDesktopInstance reply = new _NetShowingDesktopInstance(
				getDisplay(), showingDesktop != 0);
		return reply;
	}

}
