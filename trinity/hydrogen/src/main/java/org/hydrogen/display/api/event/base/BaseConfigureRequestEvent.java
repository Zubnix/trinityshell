/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.display.api.event.base;

import org.hydrogen.display.api.event.ConfigureRequestEvent;
import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.DisplayEventType;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>BaseConfigureRequestEvent</code> is a basic implementation of a
 * <code>ConfigureRequestEvent</code>. Classes wishing to implement
 * <code>ConfigureRequestEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
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

	/*****************************************
	 * @param configureNotify
	 * @param eventSource
	 * @param xSet
	 * @param ySet
	 * @param heightSet
	 * @param widthSet
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 ****************************************/
	@Inject
	protected BaseConfigureRequestEvent(@Named("ConfigureRequest") final DisplayEventType configureNotify,
										@Assisted final DisplayEventSource eventSource,
										@Assisted final boolean xSet,
										@Assisted final boolean ySet,
										@Assisted final boolean heightSet,
										@Assisted final boolean widthSet,
										@Assisted final int x,
										@Assisted final int y,
										@Assisted final int width,
										@Assisted final int height) {
		super(configureNotify, eventSource);
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
		return String
				.format("%s\tDetails: %d+%d : %dx%d - Enabled?: %s+%s : %s+%s",
						super.toString(),
						this.x,
						this.y,
						this.width,
						this.height,
						this.xSet,
						this.ySet,
						this.widthSet,
						this.heightSet);
	}
}
