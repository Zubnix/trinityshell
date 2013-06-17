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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellScene;
import org.trinity.shell.api.scene.event.ShellNodeChildAddedEvent;
import org.trinity.shell.api.scene.event.ShellNodeChildLeftEvent;
import org.trinity.shell.api.scene.event.ShellNodeEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;

import static com.google.common.base.Preconditions.checkArgument;

/***************************************
 * An {@link AbstractShellSurface} that can have child {@link ShellNode}s.
 * 
 *************************************** 
 */
@NotThreadSafe
public abstract class AbstractShellSurfaceParent extends AbstractShellSurface implements ShellSurfaceParent {

	protected AbstractShellSurfaceParent(final ShellScene shellScene,final ListeningExecutorService shellExecutor) {
		super(shellScene,shellExecutor);
	}
}