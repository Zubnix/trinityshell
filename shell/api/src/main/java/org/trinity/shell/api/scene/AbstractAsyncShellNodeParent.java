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

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

/***************************************
 * Asynchronous abstract implementation of a {@link ShellNodeParent}. Method
 * calls are delegated to the shell executor, provide in the constructor.
 * Subclasses must implement any concrete internal node manipulation.
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
public abstract class AbstractAsyncShellNodeParent extends AbstractShellNode implements ShellNodeParent {

	private final ListeningExecutorService shellExecutor;

	protected AbstractAsyncShellNodeParent(	@Nonnull ShellNodeParent shellNodeParent,
											@Nonnull @ShellScene final AsyncListenable shellScene,
											@Nonnull @ShellExecutor final ListeningExecutorService shellExecutor) {
		super(	shellNodeParent,
				shellScene,
				shellExecutor);
		this.shellExecutor = shellExecutor;
	}

	@Override
	public final ListenableFuture<Optional<ShellLayoutManager>> getLayoutManager() {
		return this.shellExecutor.submit(new Callable<Optional<ShellLayoutManager>>() {
			@Override
			public Optional<ShellLayoutManager> call() throws Exception {
				return getLayoutManagerImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getLayoutManager()}. This method is
	 * invoked by the Shell thread.
	 *
	 * @return an {@link Optional} {@link ShellLayoutManager}.
	 * @see #getLayoutManager()
	 ***************************************
	 */
	public abstract Optional<ShellLayoutManager> getLayoutManagerImpl();

	@Override
	public final ListenableFuture<Void> layout() {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return layoutImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #layout()}. This method is invoked by
	 * the Shell thread.
	 *
	 * @return null
	 * @see #layout()
	 ***************************************
	 */
	public abstract Void layoutImpl();

	@Override
	public final ListenableFuture<Void> setLayoutManager(final ShellLayoutManager shellLayoutManager) {
		return this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				return setLayoutManagerImpl(shellLayoutManager);
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #setLayoutManager(ShellLayoutManager)}.
	 * This method is invoked by the Shell thread.
	 *
	 * @return null
	 * @see #setLayoutManager(ShellLayoutManager)
	 ***************************************
	 */
	public abstract Void setLayoutManagerImpl(ShellLayoutManager shellLayoutManager);

	@Override
	public final ListenableFuture<List<ShellNode>> getChildren() {
		return this.shellExecutor.submit(new Callable<List<ShellNode>>() {
			@Override
			public List<ShellNode> call() throws Exception {
				return (List<ShellNode>) getChildrenImpl();
			}
		});
	}

	/***************************************
	 * Concrete implementation of {@link #getChildren()}. This method is invoked
	 * by the Shell thread.
	 *
	 * @return an array of {@link ShellNode}s
	 * @see #getChildren()
	 ***************************************
	 */
	public abstract List<? extends ShellNode> getChildrenImpl();
}
