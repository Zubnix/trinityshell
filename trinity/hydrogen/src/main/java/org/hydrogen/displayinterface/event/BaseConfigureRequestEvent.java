/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.displayinterface.event;

// TODO documentation
/**
 * A <code>BaseConfigureRequestEvent</code> is a basic implementation of a
 * <code>ConfigureRequestEvent</code>. Classes wishing to implement
 * <code>ConfigureRequestEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BaseConfigureRequestEvent extends BaseDisplayEvent implements
		ConfigureRequestEvent {

	private final boolean xSet;
	private final boolean ySet;
	private final boolean heightSet;
	private final boolean widthSet;
	private final int x;
	private final int y;
	private final int height;
	private final int width;

	/**
	 * 
	 * @param eventSource
	 * @param xSet
	 * @param ySet
	 * @param heightSet
	 * @param widthSet
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public BaseConfigureRequestEvent(final DisplayEventSource eventSource,
			final boolean xSet, final boolean ySet, final boolean heightSet,
			final boolean widthSet, final int x, final int y, final int width,
			final int height) {
		super(ConfigureRequestEvent.TYPE, eventSource);
		this.xSet = xSet;
		this.ySet = ySet;
		this.widthSet = widthSet;
		this.heightSet = heightSet;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isHeightSet() {
		return this.heightSet;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public boolean isWidthSet() {
		return this.widthSet;
	}

	@Override
	public boolean isXSet() {
		return this.xSet;
	}

	@Override
	public boolean isYSet() {
		return this.ySet;
	}

	@Override
	public String toString() {
		return String.format(
				"%s\tDetails: %d+%d : %dx%d - Enabled?: %s+%s : %s+%s",
				super.toString(), this.x, this.y, this.width, this.height,
				this.xSet, this.ySet, this.widthSet, this.heightSet);
	}
}
