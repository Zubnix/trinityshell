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
package org.hyperdrive.core;

import org.hydrogen.api.event.Event;

/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 * @param <R>
 */
public class RenderAreaEvent<R extends AbstractRenderArea, T extends RenderAreaEventType<R>>
		implements Event<T> {

	private final T type;
	private final R renderArea;

	/**
	 * 
	 * @param type
	 * @param renderArea
	 */
	public RenderAreaEvent(final T type, final R renderArea) {
		this.renderArea = renderArea;
		this.type = type;
	}

	/**
	 * 
	 * @return
	 */
	public R getRenderArea() {
		return this.renderArea;
	}

	@Override
	public T getType() {
		return this.type;
	}
}
