/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.protocol;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ClientPreferedGeometry implements ProtocolEvent {
	public static final ProtocolEventType TYPE = new ProtocolEventType();

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final int minWidth;
	private final int minHeight;
	private final int maxWidth;
	private final int maxHeight;
	private final int widthInc;
	private final int heightInc;
	private final boolean visible;
	private final boolean resizable;

	public ClientPreferedGeometry(final int x, final int y, final int width,
			final int height, final int minWidth, final int minHeight,
			final int maxWidth, final int maxHeight, final int widthInc,
			final int heightInc, final boolean visible, final boolean resizable) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.widthInc = widthInc;
		this.heightInc = heightInc;
		this.visible = visible;
		this.resizable = resizable;
	}

	public int getMinWidth() {
		return this.minWidth;
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public int getMaxWidth() {
		return this.maxWidth;
	}

	public int getMaxHeight() {
		return this.maxHeight;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public boolean isResizable() {
		return this.resizable;
	}

	public int getWidthInc() {
		return this.widthInc;
	}

	public int getHeightInc() {
		return this.heightInc;
	}

	@Override
	public ProtocolEventType getType() {
		return ClientPreferedGeometry.TYPE;
	}
}
