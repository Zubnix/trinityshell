/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shell.api.scene;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import java.util.List;
import java.util.concurrent.Callable;

/***************************************
 * Asynchronous abstract implementation of a {@link ShellNodeParent}. Method
 * calls are delegated to the shell executor, provide in the constructor.
 * Subclasses must implement any concrete internal node manipulation.
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
public interface DefaultShellNodeParent extends DefaultShellNode,ShellNodeParent {

	@Override
	public default ListenableFuture<Optional<ShellLayoutManager>> getLayoutManager() {
        return getShellExecutor().submit((Callable<Optional<ShellLayoutManager>>) this::getLayoutManagerImpl);
    }

	/***************************************
	 * Concrete implementation of {@link #getLayoutManager()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return an {@link Optional} {@link ShellLayoutManager}.
	 * @see #getLayoutManager()
	 ***************************************
	 */
	Optional<ShellLayoutManager> getLayoutManagerImpl();

	@Override
	public default ListenableFuture<Void> layout() {
        return getShellExecutor().submit((Callable<Void>) this::layoutImpl);
    }

	/***************************************
	 * Concrete implementation of {@link #layout()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return null
	 * @see #layout()
	 ***************************************
	 */
	Void layoutImpl();

	@Override
	public default ListenableFuture<Void> setLayoutManager(final ShellLayoutManager shellLayoutManager) {
        return getShellExecutor().submit((Callable<Void>) () -> setLayoutManagerImpl(shellLayoutManager));
    }

	/***************************************
	 * Concrete implementation of {@link #setLayoutManager(ShellLayoutManager)}.
	 * This method is invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setLayoutManager(ShellLayoutManager)
	 ***************************************
	 */
	Void setLayoutManagerImpl(ShellLayoutManager shellLayoutManager);

	@Override
	public default ListenableFuture<List<ShellNode>> getChildren() {
        return getShellExecutor().submit((Callable<List<ShellNode>>) () -> (List<ShellNode>) getChildrenImpl());
    }

	/***************************************
	 * Concrete implementation of {@link #getChildren()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return an array of {@link ShellNode}s
	 * @see #getChildren()
	 ***************************************
	 */
	List<? extends ShellNode> getChildrenImpl();
}