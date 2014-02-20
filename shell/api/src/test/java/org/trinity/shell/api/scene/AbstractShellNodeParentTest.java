package org.trinity.shell.api.scene;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.shell.api.scene.event.ShellNodeChildAddedEvent;
import org.trinity.shell.api.scene.event.ShellNodeChildLeftEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AbstractShellNodeParentTest {

	@Mock
	private ShellNodeGeometryDelegate childShellNodeGeometryDelegate0;
	@Mock
	private ShellNodeGeometryDelegate childShellNodeGeometryDelegate1;
	@Mock
	private AbstractShellNode         child0;
	@Mock
	private AbstractShellNode         child1;

	@Mock
	private Listenable shellScene;
	@Mock
	private ListeningExecutorService  shellExecutor;
	@Mock
	private ShellNodeGeometryDelegate parentShellNodeGeometryDelegate;
	private AbstractShellNodeParent   abstractShellNodeParent;

	@Before
	public void setUp() {
		when(this.shellExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final Callable<?> submittedCallable = (Callable<?>) arg0;
				final Object result = submittedCallable.call();
				return immediateFuture(result);
			}
		});

		when(this.child0.getShellNodeGeometryDelegate()).thenReturn(this.childShellNodeGeometryDelegate0);
		when(this.child1.getShellNodeGeometryDelegate()).thenReturn(this.childShellNodeGeometryDelegate1);
	}

	@Test
	public void testDoMoveImpl() {
		//given
		//an abstract shell node parent with children
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		this.abstractShellNodeParent.handleChildReparent(this.child0);
		this.abstractShellNodeParent.handleChildReparent(this.child1);

		//when
		//the abstract shell node parent is moved
		this.abstractShellNodeParent.doMove();

		//then
		//the children's position is refreshed
		verify(this.childShellNodeGeometryDelegate0).move((Coordinate) any());
		verify(this.childShellNodeGeometryDelegate1).move((Coordinate) any());
	}

	@Test
	public void testDoMoveResizeImpl() {
		//given
		//an abstract shell node parent with children and a layout manager
		final ShellLayoutManager shellLayoutManager = mock(ShellLayoutManager.class);
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		this.abstractShellNodeParent.setLayoutManager(shellLayoutManager);
		this.abstractShellNodeParent.handleChildReparent(this.child0);
		this.abstractShellNodeParent.handleChildReparent(this.child1);

		//when
		//the abstract shell node parent is moved and resized
		this.abstractShellNodeParent.doMoveResize();

		//then
		//the children's position is updated and the children are layed out.
		verify(this.childShellNodeGeometryDelegate0).move((Coordinate) any());
		verify(this.childShellNodeGeometryDelegate1).move((Coordinate) any());
		verify(shellLayoutManager).layout(this.abstractShellNodeParent);
	}

	@Test
	public void testDoResizeImpl() {
		//given
		//an abstract shell node parent with children and a layout manager
		final ShellLayoutManager shellLayoutManager = mock(ShellLayoutManager.class);
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		this.abstractShellNodeParent.setLayoutManager(shellLayoutManager);
		this.abstractShellNodeParent.handleChildReparent(this.child0);
		this.abstractShellNodeParent.handleChildReparent(this.child1);

		//when
		//the abstract shell node parent is resized
		this.abstractShellNodeParent.doResize();

		//then
		//the children are layed out.
		verify(shellLayoutManager).layout(this.abstractShellNodeParent);
	}

	@Test
	public void testLayoutImpl() {
		//given
		//an abstract shell node parent with a layout manager
		final ShellLayoutManager shellLayoutManager = mock(ShellLayoutManager.class);
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		this.abstractShellNodeParent.setLayoutManager(shellLayoutManager);

		//when
		//the abstract shell node parent is doing a layout
		this.abstractShellNodeParent.layout();

		//then
		//the layout manager is called for the abstract shell node parent
		verify(shellLayoutManager).layout(this.abstractShellNodeParent);
	}

	@Test
	public void testHandleChildReparentForChildRemoved() {
		//given
		//an abstract shell node parent with children
		//a ShellNodeChildLeftEvent listener
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		final ChildLeftListener childLeftListener = spy(new ChildLeftListener());
		this.abstractShellNodeParent.register(childLeftListener);
		this.abstractShellNodeParent.handleChildReparent(this.child0);
		this.abstractShellNodeParent.handleChildReparent(this.child1);

		//when
		//a child is removed
		this.abstractShellNodeParent.handleChildReparent(this.child0);

		//then
		//the parents children list is updated
		//the ShellNodeChildLeftEvent listener is notified
		assertEquals(1,
					 this.abstractShellNodeParent.getChildren().size());
		//TODO assert event contents
		verify(childLeftListener).handle((ShellNodeChildLeftEvent) any());
	}

	@Test
	public void testHandleChildReparentForChildAdded() {
		//given
		//an abstract shell node parent
		//a new child
		//a ShellNodeChildAddedEvent listener
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		final ChildAddedListener childAddedListener = spy(new ChildAddedListener());
		this.abstractShellNodeParent.register(childAddedListener);

		//when
		//the new child is added
		this.abstractShellNodeParent.handleChildReparent(this.child0);

		//then
		//the parents children list is updated
		//the ShellNodeChildAddedEvent listener is notified
		assertEquals(1,
					 this.abstractShellNodeParent.getChildren().size());
		//TODO assert event contents
		verify(childAddedListener).handle((ShellNodeChildAddedEvent) any());
	}

	@Test
	public void testHandleChildStackingForChildRaised() {
		//given
		//an abstract shell node parent with children
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		this.abstractShellNodeParent.handleChildReparent(this.child0);
		this.abstractShellNodeParent.handleChildReparent(this.child1);

		//when
		//a child is raised
		this.abstractShellNodeParent.handleChildStacking(this.child0,
														 true);

		//then
		//the order of the children is updated
		assertEquals(1,
					 this.abstractShellNodeParent.getChildren().indexOf(this.child0));
		assertEquals(0,
					 this.abstractShellNodeParent.getChildren().indexOf(this.child1));
	}

	@Test
	public void testHandleChildStackingForChildLowered() {
		//given
		//an abstract shell node parent with children
		this.abstractShellNodeParent = new AbstractShellNodeParent(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return AbstractShellNodeParentTest.this.parentShellNodeGeometryDelegate;
			}
		};
		this.abstractShellNodeParent.handleChildReparent(this.child0);
		this.abstractShellNodeParent.handleChildReparent(this.child1);

		//when
		//a child is lowered
		this.abstractShellNodeParent.handleChildStacking(this.child1,
														 false);

		//then
		//the order of the children is updated
		assertEquals(1,
					 this.abstractShellNodeParent.getChildren().indexOf(this.child0));
		assertEquals(0,
					 this.abstractShellNodeParent.getChildren().indexOf(this.child1));
	}
}

class ChildLeftListener {
	@Subscribe
	public void handle(final ShellNodeChildLeftEvent event) {

	}
}

class ChildAddedListener {
	@Subscribe
	public void handle(final ShellNodeChildAddedEvent event) {

	}
}