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
import org.trinity.shell.api.scene.ShellNode;

import com.google.common.eventbus.EventBus;

/***************************************
 * Dispatches {@link DisplayEvent}s to the corresponding {@link ShellNode}s. It
 * does so by taking an event from the {@link DisplayServer} and posting it on
 * the {@link EventBus} of the associated {@code ShellNode}.
 * 
 *************************************** 
 */
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
	 * should be posted on the given {@code EventBus}.
	 * 
	 * @param eventBus
	 *            The {@link EventBus} that will receive the forwarded events.
	 * @param displayEventTarget
	 *            An event target as specified by
	 *            {@link DisplayEvent#getDisplayEventTarget()}.
	 *************************************** 
	 */
	void registerDisplayEventTarget(final EventBus eventBus,
									final Object displayEventTarget);

	/***************************************
	 * Notifies the shell event dispatcher that events with the given target
	 * should no longer be posted on the given node {@code EventBus}.
	 * 
	 * @param eventBus
	 *            The {@link EventBus} that will no longer receive the forwarded
	 *            events.
	 * @param displayEventTarget
	 *            An event target as specified by
	 *            {@link DisplayEvent#getDisplayEventTarget()}.
	 *************************************** 
	 */
	void unregisterDisplayEventTarget(	EventBus eventBus,
										Object displayEventTarget);

	/***************************************
	 * Notifies the shell event dispatcher that events with the given target
	 * should no longer be posted on all associated {@code EventBus}ses.
	 * 
	 * @param displayEventTarget
	 *            An event target as specified by
	 *            {@link DisplayEvent#getDisplayEventTarget()}.
	 *************************************** 
	 */
	void unregisterAllDisplayEventTarget(Object displayEventTarget);
}