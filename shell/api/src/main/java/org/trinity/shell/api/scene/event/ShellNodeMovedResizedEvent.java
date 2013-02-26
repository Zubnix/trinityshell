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
package org.trinity.shell.api.scene.event;

import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeTransformation;

/***************************************
 * Informs that the {@link ShellNode} that emits this event, is moved and
 * resized.
 * 
 *************************************** 
 */
public class ShellNodeMovedResizedEvent extends ShellNodeEvent {

	/**
	 * Create a new {@code ShellNodeChildAddedEvent} with the given
	 * {@code ShellNode} as the node that emitted the event, and the given
	 * {@code ShellNodeTransformation} as the details coming from the given node
	 * e.g. {@link ShellNode#toGeoTransformation()}
	 * 
	 * @param shellNode
	 *            the emitting {@link ShellNode}
	 * @param shellNodeTransformation
	 *            a {@link ShellNodeTransformation}
	 */
	public ShellNodeMovedResizedEvent(	final ShellNode shellNode,
										final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}