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
package org.hyperdrive.widget;

import org.hyperdrive.core.RenderAreaEvent;
import org.hyperdrive.core.RenderAreaEventType;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 * @param <W>
 */
public class WidgetEvent<R extends BaseWidget> extends
		RenderAreaEvent<R, RenderAreaEventType<R>> {

	public static final RenderAreaEventType<BaseWidget> WIDGET_INITIALIZED = new RenderAreaEventType<BaseWidget>();

	/**
	 * 
	 * @param type
	 * @param renderArea
	 */
	public WidgetEvent(final RenderAreaEventType<R> type, final R renderArea) {
		super(type, renderArea);
	}
}
