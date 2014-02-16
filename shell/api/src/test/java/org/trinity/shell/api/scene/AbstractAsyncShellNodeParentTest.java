package org.trinity.shell.api.scene;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import java.util.List;
import java.util.concurrent.Callable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AbstractAsyncShellNodeParentTest {

	@Mock
	private Listenable shellScene;
	@Mock
	private ListeningExecutorService shellExecutor;

	@Test
	public void testAsyncDelegations() {
		//given
		//an abstract async shellnode parent with empty implementations
		final AbstractAsyncShellNodeParent abstractAsyncShellNodeParent = new AbstractAsyncShellNodeParent(this.shellScene,
																										   this.shellExecutor) {
			@Override
			public Optional<ShellLayoutManager> getLayoutManagerImpl() {
				return null;
			}

			@Override
			public Void layoutImpl() {
				return null;
			}

			@Override
			public Void setLayoutManagerImpl(final ShellLayoutManager shellLayoutManager) {
				return null;
			}

			@Override
			public List<? extends ShellNode> getChildrenImpl() {
				return null;
			}

			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};

		//when
		//any of it's async methods is called
		abstractAsyncShellNodeParent.getChildren();//1
		abstractAsyncShellNodeParent.getLayoutManager();//2
		abstractAsyncShellNodeParent.layout();//3
		abstractAsyncShellNodeParent.setLayoutManager(null);//4

		//then
		//the call is delegated to it's executor.
		verify(this.shellExecutor,
			   times(4)).submit((Callable<Object>) any());
	}
}
