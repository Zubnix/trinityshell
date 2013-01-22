/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.surface;

import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.event.DisplayEvent;

import com.google.common.eventbus.EventBus;

public interface ShellDisplayEventDispatcher {

	/***************************************
	 * Read a {@link DisplayEvent} from
	 * {@link DisplayServer#getNextDisplayEvent()} and forward it to the
	 * associated {@link EventBus}ses.
	 * 
	 * @param block
	 *            Blocks the calling thread if no event is available.
	 *************************************** 
	 */
	void dispatchDisplayEvent(final boolean block);

	/***************************************
	 * Notifies the shell event dispatcher that events with the given target
	 * should be forwarded to the given {@code EventBus}.
	 * 
	 * @param eventBus
	 *            The {@link EventBus} that will receive the forwarded events.
	 * @param displayEventTarget
	 *            An event target as specified by
	 *            {@link DisplayEvent#getDisplayEventTarget()}.
	 *************************************** 
	 */
	void registerDisplayEventSourceListener(final EventBus eventBus,
											final Object displayEventTarget);

	/***************************************
	 * Notifies the shell event dispatcher that events with the given target
	 * should no longer be forwarded to the given node {@code EventBus}.
	 * 
	 * @param eventBus
	 *            The {@link EventBus} that will no longer receive the forwarded
	 *            events.
	 * @param displayEventTarget
	 *            An event target as specified by
	 *            {@link DisplayEvent#getDisplayEventTarget()}.
	 *************************************** 
	 */
	void unregisterDisplayEventSourceListener(	EventBus eventBus,
												Object displayEventTarget);

	/***************************************
	 * Notifies the shell event dispatcher that events with the given target
	 * should no longer be forwarded to all associated {@code EventBus}ses.
	 * 
	 * @param displayEventTarget
	 *            An event target as specified by
	 *            {@link DisplayEvent#getDisplayEventTarget()}.
	 *************************************** 
	 */
	void unregisterAllDisplayEventSourceListeners(Object displayEventTarget);
}