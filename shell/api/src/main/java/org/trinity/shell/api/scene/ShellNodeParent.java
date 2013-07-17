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
package org.trinity.shell.api.scene;

import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

/***************************************
 * A {@link ShellNode} that can have child <code>ShellNode</code>s. A node's
 * parent can be changed by calling {@link ShellNode#setParent(ShellNodeParent)}
 * followed by either {@link ShellNode#doReparent()} or
 * {@link ShellNode#requestReparent()}. <code>doReparent</code> guarantees that
 * the child will have the desired parent as its new parent.
 * <code>requestReparent</code> delegates the reparenting to any subscribed
 * child node listener, which can be, for example, the current parent's
 * {@link ShellLayoutManager}.
 *
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
public interface ShellNodeParent extends ShellNode {

	/***************************************
	 * The layout manager that his parent will use to layout it's children.
	 * Children are not laid out automatically. See {@link #layout()}.
	 *
	 * @return A {@link ShellLayoutManager}.
	 ***************************************
	 */
	ListenableFuture<Optional<ShellLayoutManager>> getLayoutManager();

	/***************************************
	 * Layout all child <code>ShellNode</code>s.
	 ***************************************
	 */
	ListenableFuture<Void> layout();

	/***************************************
	 * Change the layout manager of this parent to the desired layout manager.
	 *
	 * @param shellLayoutManager
	 *            A {@link ShellLayoutManager}.f
	 ***************************************
	 */
	ListenableFuture<Void> setLayoutManager(ShellLayoutManager shellLayoutManager);

	/***************************************
	 * The direct child nodes of this parent.
	 *
	 * @return an array of {@link ShellNode}s
	 ***************************************
	 */
	ListenableFuture<List<ShellNode>> getChildren();
}
