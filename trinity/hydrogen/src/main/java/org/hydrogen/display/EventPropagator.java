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
package org.hydrogen.display;

import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.event.ConfigureNotifyEvent;
import org.hydrogen.api.display.event.ConfigureRequestEvent;
import org.hydrogen.api.display.event.DestroyNotifyEvent;
import org.hydrogen.api.display.event.DisplayEvent;
import org.hydrogen.api.display.event.FocusNotifyEvent;
import org.hydrogen.api.display.event.MapRequestEvent;
import org.hydrogen.api.display.event.MouseEnterLeaveNotifyEvent;
import org.hydrogen.api.display.event.PropertyChangedNotifyEvent;
import org.hydrogen.api.display.event.UnmappedNotifyEvent;

/**
 * The <code>EventPropagation</code> functions as a way to express interest in
 * certain <code>DisplayEvent</code>s. A programmer can pass any number of
 * different <code>EventPropagation</code>s to a {@link PlatformRenderArea} to
 * inform the native display which <code>DisplayEvent</code>s it wants to
 * receive from that <code>PlatformRenderArea</code>.
 * <p>
 * Native events will be translated to their respective {@link DisplayEvent}
 * equivalents.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class EventPropagator {

	/**
	 * Indicates that we want to receive {@link PropertyChangedNotifyEvent}s
	 * whenever a property of the <code>PlatformRenderArea</code> is changed.
	 */
	public static final EventPropagator NOTIFY_CHANGED_WINDOW_PROPERTY = new EventPropagator();

	/**
	 * Indicates that we want to receive {@link MouseEnterLeaveNotifyEvent}s
	 * whenever a mouse cursors enters a <code>PlatformRenderArea</code>.
	 */
	public static final EventPropagator NOTIFY_MOUSE_ENTER = new EventPropagator();

	/**
	 * Indicates that we want to receive {@link MouseEnterLeaveNotifyEvent}s
	 * whenever a mouse cursor leaves a <code>PlatformRenderArea</code>.
	 */
	public static final EventPropagator NOTIFY_MOUSE_LEAVE = new EventPropagator();

	/**
	 * Indicates that we want to receive {@link ConfigureNotifyEvent}s,
	 * {@link DestroyNotifyEvent}s, {@link UnmappedNotifyEvent}s whenever the
	 * structure of the <code>PlatformRenderArea</code> changes.
	 */
	public static final EventPropagator NOTIFY_CHANGED_WINDOW_GEOMETRY = new EventPropagator();

	/**
	 * Indicates that the we want to receive {@link ConfigureRequestEvent}s and
	 * {@link MapRequestEvent}s of the child <code>PlatformRenderArea</code>s
	 * whenever the structure of these childs of the
	 * <code>PlatformRenderArea</code> where this mask is set on, is changed.
	 */
	public static final EventPropagator REDIRECT_CHILD_WINDOW_GEOMTRY_CHANGES = new EventPropagator();

	/**
	 * Indicates that we want to receive {@link FocusNotifyEvent}s whenever the
	 * focus of the <code>PlatformRenderArea</code> changes.
	 */
	public static final EventPropagator NOTIFY_CHANGED_WINDOW_FOCUS = new EventPropagator();

	protected EventPropagator() {
	}

	// TODO mapnotify, reparentnotify, geometrynotify & react on it in the
	// hypdrive-core library.
}
