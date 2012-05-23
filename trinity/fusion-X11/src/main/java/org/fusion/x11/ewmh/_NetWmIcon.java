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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.fusion.x11.core.FlexDataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceInfo;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.ewmh._NetWmIconInstance.WmIcon;
import org.trinity.core.display.api.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWmIcon extends XPropertyXAtom<_NetWmIconInstance> {
	// CARDINAL[][2+n]/32

	/**
	 * 
	 * @param display
	 * 
	 */
	public _NetWmIcon(final XDisplay display) {
		super(display, EwmhAtoms.NET_WM_ICON_ATOM_NAME);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final _NetWmIconInstance propertyInstance) {
		final WmIcon[] wmIcons = propertyInstance.getWmIcons();
		// should be large enough...
		final List<Integer> intData = new ArrayList<Integer>(10 * 50 * 50);
		for (final WmIcon wmIcon : wmIcons) {
			final int width = wmIcon.getWidth();
			final int height = wmIcon.getHeight();
			final byte[] pixels = wmIcon.getArgbPixels();
			intData.add(Integer.valueOf(width));
			intData.add(Integer.valueOf(height));

			for (final byte pixel : pixels) {
				intData.add(Integer.valueOf(pixel));
			}

		}
		final IntDataContainer rawDataContainer = new IntDataContainer(
				intData.size());
		for (final Integer data : intData) {
			rawDataContainer.writeDataBlock(data);
		}

		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public _NetWmIconInstance getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexDataContainer propertyDataContainer) {
		final long length = propertyInstanceInfo.getLength();
		long read = 0;

		final List<WmIcon> icons = new LinkedList<_NetWmIconInstance.WmIcon>();
		while (length > read) {
			final int width = (int) propertyDataContainer.readUnsignedInt();
			final int height = (int) propertyDataContainer.readUnsignedInt();
			final int nroPixels = width * height;

			// each pixel is an int and thus consists of 4 bytes.
			read += 8 + (nroPixels * 4);

			final byte[] pixels = new byte[nroPixels * 4];
			propertyDataContainer.getBuffer().get(pixels);

			final WmIcon icon = new WmIcon(width, height, pixels);
			icons.add(icon);
		}

		final _NetWmIconInstance reply = new _NetWmIconInstance(getDisplay(),
				icons.toArray(new WmIcon[icons.size()]));
		return reply;
	}

}
