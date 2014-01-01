package org.trinity.shell.api.scene;

import com.google.common.base.Optional;
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
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.event.*;

import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AbstractShellNodeTest {

	@Mock
	private AsyncListenable          shellScene;
	@Mock
	private ListeningExecutorService shellExecutor;

	private AbstractShellNode abstractShellNode;

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
	}

	@Test
	public void testCancelPendingMoveImpl() {
		//given
		//an abstract shell node
		//a desired position
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		this.abstractShellNode.setPosition(120,
										   130);

		//when
		//the move is canceled
		this.abstractShellNode.cancelPendingMoveImpl();

		//then
		//the desired position is reset to the previous position
		assertEquals(new Coordinate(0,
									0),
					 this.abstractShellNode.getDesiredPosition());
	}

	@Test
	public void testCancelPendingResizeImpl() {
		//given
		//an abstract shell node
		//a desired size
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		this.abstractShellNode.setSize(100,
									   200);

		//when
		//the resize is canceled
		this.abstractShellNode.cancelPendingResizeImpl();

		//then
		//the desired size is reset to the previous size
		assertEquals(new Size(5,
							  5),
					 this.abstractShellNode.getDesiredSize());
	}


	@Test
	public void testDoDestroyImpl() {
		//given
		//an abstract shell node
		//a destroy listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final DestroyListener destroyListener = spy(new DestroyListener());
		this.abstractShellNode.register(destroyListener);


		//when
		//the abstract shell node is marked as destroyed
		this.abstractShellNode.doDestroyImpl();

		//then
		//subsequent calls to isDestroyed return true
		//the destroy listener is notified
		assertTrue(this.abstractShellNode.isDestroyedImpl());
		//TODO verify event contents
		verify(destroyListener).handle((ShellNodeDestroyedEvent) any());
	}

	@Test
	public void testDoHideImpl() {
		//given
		//an abstract shell node
		//a hide listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final HideListener hideListener = spy(new HideListener());
		this.abstractShellNode.register(hideListener);

		//when
		//the abstract shell node is made invisible
		this.abstractShellNode.doHideImpl();

		//then
		//subsequent calls to isVisible return false
		//the hide listener is notified
		assertFalse(this.abstractShellNode.isVisibleImpl());
		//TODO verify event contents
		verify(hideListener).handle((ShellNodeHiddenEvent) any());
	}

	@Test
	public void testDoLowerImpl() {
		//given
		//an abstract shell node with a parent
		//a lower listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final AbstractShellNodeParent abstractShellNodeParent = mock(AbstractShellNodeParent.class);
		this.abstractShellNode.setParentImpl(Optional.of(abstractShellNodeParent));
		this.abstractShellNode.doReparentImpl();
		final LowerListener lowerListener = spy(new LowerListener());
		this.abstractShellNode.register(lowerListener);

		//when
		//the abstract shell node is lowered
		this.abstractShellNode.doLowerImpl();

		//then
		//its parent is notified
		//the lower listener is notified
		verify(abstractShellNodeParent).handleChildStacking(this.abstractShellNode,
															false);
		//TODO verify event contents
		verify(lowerListener).handle((ShellNodeLoweredEvent) any());
	}

	@Test
	public void testDoMoveImpl() {
		//given
		//an abstract shell node with a geometry delegate
		//a move listener
		final ShellNodeGeometryDelegate shellNodeGeometryDelegate = mock(ShellNodeGeometryDelegate.class);
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return shellNodeGeometryDelegate;
			}
		};
		final MoveListener moveListener = spy(new MoveListener());
		this.abstractShellNode.register(moveListener);


		//when
		//the abstract shell node is moved
		this.abstractShellNode.setPosition(123,
										   456);
		this.abstractShellNode.doMoveImpl();

		//then
		//it's position is updated
		//the geometry delegate is notified
		//the move listener is notified
		assertEquals(new Coordinate(123,
									456),
					 this.abstractShellNode.getPositionImpl());
		verify(shellNodeGeometryDelegate).move(eq(new Coordinate(123,
																 456)));
		//TODO verify event contents
		verify(moveListener).handle((ShellNodeMovedEvent) any());
	}

	@Test
	public void testDoMoveResizeImpl() {
		//given
		//an abstract shell node with a geometry delegate
		//a move resize listener
		final ShellNodeGeometryDelegate shellNodeGeometryDelegate = mock(ShellNodeGeometryDelegate.class);
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return shellNodeGeometryDelegate;
			}
		};
		final MoveResizeListener moveResizeListener = spy(new MoveResizeListener());
		this.abstractShellNode.register(moveResizeListener);

		//when
		//it's size an position is updated

		this.abstractShellNode.setSizeImpl(100,
										   200);
		this.abstractShellNode.setPositionImpl(123,
											   456);
		this.abstractShellNode.doMoveResizeImpl();

		//then
		//its size is updated
		//tis position is updated
		//the geometry delegate is notified
		//the move resize listener is notified
		assertEquals(new Coordinate(123,
									456),
					 this.abstractShellNode.getPositionImpl());
		assertEquals(new Size(100,
							  200),
					 this.abstractShellNode.getSizeImpl());
		verify(shellNodeGeometryDelegate).moveResize(eq(new Coordinate(123,
																	   456)),
													 eq(new Size(100,
																 200)));
		//TODO verify event contents
		verify(moveResizeListener).handle((ShellNodeMovedResizedEvent) any());
	}

	@Test
	public void testDoRaiseImpl() {
		//given
		//an abstract shell node with a parent
		//a raise listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final AbstractShellNodeParent abstractShellNodeParent = mock(AbstractShellNodeParent.class);
		this.abstractShellNode.setParentImpl(Optional.of(abstractShellNodeParent));
		this.abstractShellNode.doReparentImpl();
		final RaiseListener raiseListener = spy(new RaiseListener());
		this.abstractShellNode.register(raiseListener);

		//when
		//the abstract shell node is raised
		this.abstractShellNode.doRaiseImpl();

		//then
		//the parent is notified
		//the raise listener is notified
		verify(abstractShellNodeParent).handleChildStacking(this.abstractShellNode,
															true);
		//TODO verify event contents
		verify(raiseListener).handle((ShellNodeRaisedEvent) any());
	}

	@Test
	public void testDoReparentImpl() {
		//given
		//an abstract shell node with a parent
		//a new parent
		//a reparent listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final AbstractShellNodeParent oldAbstractShellNodeParent = mock(AbstractShellNodeParent.class);
		this.abstractShellNode.setParentImpl(Optional.of(oldAbstractShellNodeParent));
		this.abstractShellNode.doReparentImpl();
		final AbstractShellNodeParent newAbstractShellNodeParent = mock(AbstractShellNodeParent.class);
		final ReparentListener reparentListener = spy(new ReparentListener());
		this.abstractShellNode.register(reparentListener);

		//when
		//the abstract shell node is reparented
		this.abstractShellNode.setParentImpl(Optional.of(newAbstractShellNodeParent));
		this.abstractShellNode.doReparentImpl();

		//then
		//the abstract shell node's parent is updated
		//the old parent is notified
		//the new parent is notified
		//the reparent listener is notified
		assertEquals(newAbstractShellNodeParent,
					 this.abstractShellNode.getParentImpl().get());
		verify(oldAbstractShellNodeParent).handleChildReparent(this.abstractShellNode);
		verify(newAbstractShellNodeParent).handleChildReparent(this.abstractShellNode);
		//TODO verify event contents
		verify(reparentListener).handle((ShellNodeReparentedEvent) any());
	}

	@Test
	public void testDoResizeImpl() {
		//given
		//an abstract shell node with a geometry delegate
		//a resize listener
		final ShellNodeGeometryDelegate shellNodeGeometryDelegate = mock(ShellNodeGeometryDelegate.class);
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return shellNodeGeometryDelegate;
			}
		};
		final ResizeListener resizeListener = spy(new ResizeListener());
		this.abstractShellNode.register(resizeListener);

		//when
		//the abstract shell node is resized
		this.abstractShellNode.setSizeImpl(100,
										   200);
		this.abstractShellNode.doResizeImpl();

		//then
		//the abstract shell node's size is updated
		//the geometry delegate is notified
		//the resize listener is notified
		assertEquals(new Size(100,
							  200),
					 this.abstractShellNode.getSizeImpl());
		verify(shellNodeGeometryDelegate).resize(eq(new Size(100,
															 200)));
		//TODO verify event contents
		verify(resizeListener).handle((ShellNodeResizedEvent) any());
	}

	@Test
	public void testDoShowImpl() {
		//given
		//an abstract shell node
		//a show listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final ShowListener showListener = spy(new ShowListener());
		this.abstractShellNode.register(showListener);

		//when
		//the abstract shell node is made visible
		this.abstractShellNode.doShowImpl();

		//then
		//the abstract shell node is updated
		//the show listener is notified
		assertTrue(this.abstractShellNode.isVisibleImpl());
		//TODO verify event contents
		verify(showListener).handle((ShellNodeShowedEvent) any());
	}

	@Test
	public void testRequestHideImpl() {
		//given
		//an abstract shell node
		//a request hide listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestHideListener requestHideListener = spy(new RequestHideListener());
		this.abstractShellNode.register(requestHideListener);

		//when
		//the abstract shell node is requested to be made invisible
		this.abstractShellNode.requestHideImpl();

		//then
		//the request hide listener is notified
		//TODO verify event contents
		verify(requestHideListener).handle((ShellNodeHideRequestEvent) any());
	}

	@Test
	public void testRequestLowerImpl() {
		//given
		//an abstract shell node
		//a request lower listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestLowerListener requestLowerListener = spy(new RequestLowerListener());
		this.abstractShellNode.register(requestLowerListener);

		//when
		//the abstract shell node is requested to be lowered
		this.abstractShellNode.requestLowerImpl();

		//then
		//the request lower listener is notified
		//TODO verify event contents
		verify(requestLowerListener).handle((ShellNodeLowerRequestEvent) any());
	}

	@Test
	public void testRequestMoveImpl() {
		//given
		//an abstract shell node
		//a request move listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestMoveListener requestMoveListener = spy(new RequestMoveListener());
		this.abstractShellNode.register(requestMoveListener);

		//when
		//the abstract shell node is requested to be moved
		this.abstractShellNode.requestMoveImpl();

		//then
		//the request move listener is notified
		//TODO verify event contents
		verify(requestMoveListener).handle((ShellNodeMoveRequestEvent) any());
	}

	@Test
	public void testRequestMoveResizeImpl() {
		//given
		//an abstract shell node
		//a request move resize listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestMoveResizeListener requestMoveResizeListener = spy(new RequestMoveResizeListener());
		this.abstractShellNode.register(requestMoveResizeListener);

		//when
		//the abstract shell node is requested to be moved and resized
		this.abstractShellNode.requestMoveResizeImpl();

		//then
		//the request move resize listener is notified
		//TODO verify event contents
		verify(requestMoveResizeListener).handle((ShellNodeMoveResizeRequestEvent) any());
	}

	@Test
	public void testRequestRaiseImpl() {
		//given
		//an abstract shell node
		//a request raise listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestRaiseListener requestRaiseListener = spy(new RequestRaiseListener());
		this.abstractShellNode.register(requestRaiseListener);

		//when
		//the abstract shell node is requested to be raised
		this.abstractShellNode.requestRaiseImpl();

		//then
		//the request raise listener is notified
		//TODO verify event contents
		verify(requestRaiseListener).handle((ShellNodeRaiseRequestEvent) any());
	}

	@Test
	public void testRequestReparentImpl(){
		//given
		//an abstract shell node
		//a request reparent listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestReparentListener requestReparentListener = spy(new RequestReparentListener());
		this.abstractShellNode.register(requestReparentListener);

		//when
		//the abstract shell node is requested to be reparented
		this.abstractShellNode.requestReparentImpl();

		//then
		//the request reparent listener is notified
		//TODO verify event contents
		verify(requestReparentListener).handle((ShellNodeReparentRequestEvent) any());
	}

	@Test
	public void testRequestResizeImpl(){
		//given
		//an abstract shell node
		//a request resize listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestResizeListener requestResizeListener = spy(new RequestResizeListener());
		this.abstractShellNode.register(requestResizeListener);

		//when
		//the abstract shell node is requested to be resized
		this.abstractShellNode.requestResizeImpl();

		//then
		//the request resize listener is notified
		//TODO verify event contents
		verify(requestResizeListener).handle((ShellNodeResizeRequestEvent) any());
	}

	@Test
	public void testRequestShowImpl(){
		//given
		//an abstract shell node
		//a request show listener
		this.abstractShellNode = new AbstractShellNode(this.shellScene,
													   this.shellExecutor) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestShowListener requestShowListener = spy(new RequestShowListener());
		this.abstractShellNode.register(requestShowListener);

		//when
		//the abstract shell node is requested to be made visible
		this.abstractShellNode.requestShowImpl();

		//then
		//the request show listener is notified
		//TODO verify event contents
		verify(requestShowListener).handle((ShellNodeShowRequestEvent) any());
	}
}

class DestroyListener {
	@Subscribe
	public void handle(final ShellNodeDestroyedEvent shellNodeDestroyedEvent) {

	}
}

class HideListener {
	@Subscribe
	public void handle(final ShellNodeHiddenEvent shellNodeHiddenEvent) {

	}
}

class LowerListener {
	@Subscribe
	public void handle(final ShellNodeLoweredEvent shellNodeLoweredEvent) {

	}
}

class MoveListener {
	@Subscribe
	public void handle(final ShellNodeMovedEvent shellNodeMovedEvent) {

	}
}

class MoveResizeListener {
	@Subscribe
	public void handle(final ShellNodeMovedResizedEvent shellNodeMovedResizedEvent) {

	}
}

class RaiseListener {
	@Subscribe
	public void handle(final ShellNodeRaisedEvent shellNodeRaisedEvent) {

	}
}

class ReparentListener {
	@Subscribe
	public void handle(final ShellNodeReparentedEvent shellNodeReparentedEvent) {

	}
}

class ResizeListener {
	@Subscribe
	public void handle(final ShellNodeResizedEvent shellNodeResizedEvent) {

	}
}

class ShowListener {
	@Subscribe
	public void handle(final ShellNodeShowedEvent shellNodeShowedEvent) {

	}
}

class RequestHideListener {
	@Subscribe
	public void handle(final ShellNodeHideRequestEvent shellNodeHideRequestEvent) {

	}
}

class RequestLowerListener {
	@Subscribe
	public void handle(final ShellNodeLowerRequestEvent shellNodeLowerRequestEvent) {

	}
}

class RequestMoveListener {
	@Subscribe
	public void handle(final ShellNodeMoveRequestEvent shellNodeMoveRequestEvent) {

	}
}

class RequestMoveResizeListener {
	@Subscribe
	public void handle(final ShellNodeMoveResizeRequestEvent shellNodeMoveResizeRequestEvent) {

	}
}

class RequestRaiseListener {
	@Subscribe
	public void handle(final ShellNodeRaiseRequestEvent shellNodeRaiseRequestEvent) {

	}
}

class RequestReparentListener {
	@Subscribe
	public void handle(final ShellNodeReparentRequestEvent  shellNodeReparentRequestEvent){

	}
}

class RequestResizeListener {
	@Subscribe
	public void handle(final ShellNodeResizeRequestEvent shellNodeResizeRequestEvent){

	}
}

class RequestShowListener {
	@Subscribe
	public void handle(final ShellNodeShowRequestEvent shellNodeShowRequestEvent){

	}
}
