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

	private final boolean configureX;
	private final boolean configureY;
	private final boolean configureHeight;
	private final boolean configureWidth;
	private final int configureValueX;
	private final int configureValueY;
	private final int configureValueHeight;
	private final int configureValueWidth;

	/**
	 * 
	 * @param eventSource
	 * @param configureX
	 * @param configureY
	 * @param configureHeight
	 * @param configureWidth
	 * @param configureValueX
	 * @param configureValueY
	 * @param configureValueWidth
	 * @param configureValueHeight
	 */
	public BaseConfigureRequestEvent(final DisplayEventSource eventSource,
			final boolean configureX, final boolean configureY,
			final boolean configureHeight, final boolean configureWidth,
			final int configureValueX, final int configureValueY,
			final int configureValueWidth, final int configureValueHeight) {
		super(ConfigureRequestEvent.TYPE, eventSource);
		this.configureX = configureX;
		this.configureY = configureY;
		this.configureWidth = configureWidth;
		this.configureHeight = configureHeight;
		this.configureValueX = configureValueX;
		this.configureValueY = configureValueY;
		this.configureValueWidth = configureValueWidth;
		this.configureValueHeight = configureValueHeight;
	}

	@Override
	public boolean configureHeight() {
		return this.configureHeight;
	}

	@Override
	public int configureValueHeight() {
		return this.configureValueHeight;
	}

	@Override
	public int configureValueWidth() {
		return this.configureValueWidth;
	}

	@Override
	public int configureValueX() {
		return this.configureValueX;
	}

	@Override
	public int configureValueY() {
		return this.configureValueY;
	}

	@Override
	public boolean configureWidth() {
		return this.configureWidth;
	}

	@Override
	public boolean configureX() {
		return this.configureX;
	}

	@Override
	public boolean configureY() {
		return this.configureY;
	}

	@Override
	public String toString() {
		return String.format(
				"%s\tDetails: %d+%d : %dx%d - Enabled?: %s+%s : %s+%s",
				super.toString(), this.configureValueX, this.configureValueY,
				this.configureValueWidth, this.configureValueHeight,
				this.configureX, this.configureY, this.configureWidth,
				this.configureHeight);
	}
}
