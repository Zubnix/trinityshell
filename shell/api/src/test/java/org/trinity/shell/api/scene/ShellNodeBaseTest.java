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
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.shell.api.scene.event.*;

import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Point;
import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ShellNodeBaseTest {

	@Mock
	private Listenable shellScene;
	@Mock
	private ListeningExecutorService shellExecutor;

	private ShellNodeBase shellNodeBase;

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
	public void testCancelPendingMove() {
		//given
		//an abstract shell node
		//a desired position
		this.shellNodeBase = new ShellNodeBase(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		this.shellNodeBase.setPosition(120,
										   130);

		//when
		//the move is canceled
		this.shellNodeBase.cancelPendingMove();

		//then
		//the desired position is reset to the previous position
		assertEquals(new Point(0,
							   0),
					 this.shellNodeBase.getDesiredPosition());
	}

	@Test
	public void testCancelPendingResize() {
		//given
		//an abstract shell node
		//a desired size
		this.shellNodeBase = new ShellNodeBase(this.shellScene) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		this.shellNodeBase.setSize(100,
									   200);

		//when
		//the resize is canceled
		this.shellNodeBase.cancelPendingResize();

		//then
		//the desired size is reset to the previous size
		assertEquals(new Dimension(5,
								   5),
					 this.shellNodeBase.getDesiredSize());
	}


	@Test
	public void testDoDestroy() {
		//given
		//an abstract shell node
		//a destroy listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final DestroyListener destroyListener = spy(new DestroyListener());
		this.shellNodeBase.register(destroyListener);


		//when
		//the abstract shell node is marked as destroyed
		this.shellNodeBase.doDestroy();

		//then
		//subsequent calls to isDestroyed return true
		//the destroy listener is notified
		assertTrue(this.shellNodeBase.isDestroyed());
		//TODO verify event contents
		verify(destroyListener).handle((ShellNodeDestroyedEvent) any());
	}

	@Test
	public void testDoHide() {
		//given
		//an abstract shell node
		//a hide listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final HideListener hideListener = spy(new HideListener());
		this.shellNodeBase.register(hideListener);

		//when
		//the abstract shell node is made invisible
		this.shellNodeBase.doHide();

		//then
		//subsequent calls to isVisible return false
		//the hide listener is notified
		assertFalse(this.shellNodeBase.isVisible());
		//TODO verify event contents
		verify(hideListener).handle((ShellNodeHiddenEvent) any());
	}

	@Test
	public void testDoLower() {
		//given
		//an abstract shell node with a parent
		//a lower listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final ShellNodeParentBase abstractShellNodeParent = mock(ShellNodeParentBase.class);
		this.shellNodeBase.setParent(Optional.<ShellNodeParent>of(abstractShellNodeParent));
		this.shellNodeBase.doReparent();
		final LowerListener lowerListener = spy(new LowerListener());
		this.shellNodeBase.register(lowerListener);

		//when
		//the abstract shell node is lowered
		this.shellNodeBase.doLower();

		//then
		//its parent is notified
		//the lower listener is notified
		verify(abstractShellNodeParent).handleChildStacking(this.shellNodeBase,
															false);
		//TODO verify event contents
		verify(lowerListener).handle((ShellNodeLoweredEvent) any());
	}

	@Test
	public void testDoMove() {
		//given
		//an abstract shell node with a geometry delegate
		//a move listener
		final ShellNodeGeometryDelegate shellNodeGeometryDelegate = mock(ShellNodeGeometryDelegate.class);
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return shellNodeGeometryDelegate;
			}
		};
		final MoveListener moveListener = spy(new MoveListener());
		this.shellNodeBase.register(moveListener);


		//when
		//the abstract shell node is moved
		this.shellNodeBase.setPosition(123,
										   456);
		this.shellNodeBase.doMove();

		//then
		//it's position is updated
		//the geometry delegate is notified
		//the move listener is notified
		assertEquals(new Point(123,
							   456),
					 this.shellNodeBase.getPosition());
		verify(shellNodeGeometryDelegate).move(eq(new Point(123,
															456)));
		//TODO verify event contents
		verify(moveListener).handle((ShellNodeMovedEvent) any());
	}

	@Test
	public void testDoRaise() {
		//given
		//an abstract shell node with a parent
		//a raise listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final ShellNodeParentBase abstractShellNodeParent = mock(ShellNodeParentBase.class);
		this.shellNodeBase.setParent(Optional.<ShellNodeParent>of(abstractShellNodeParent));
		this.shellNodeBase.doReparent();
		final RaiseListener raiseListener = spy(new RaiseListener());
		this.shellNodeBase.register(raiseListener);

		//when
		//the abstract shell node is raised
		this.shellNodeBase.doRaise();

		//then
		//the parent is notified
		//the raise listener is notified
		verify(abstractShellNodeParent).handleChildStacking(this.shellNodeBase,
															true);
		//TODO verify event contents
		verify(raiseListener).handle((ShellNodeRaisedEvent) any());
	}

	@Test
	public void testDoReparent() {
		//given
		//an abstract shell node with a parent
		//a new parent
		//a reparent listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final ShellNodeParentBase oldAbstractShellNodeParent = mock(ShellNodeParentBase.class);
		this.shellNodeBase.setParent(Optional.<ShellNodeParent>of(oldAbstractShellNodeParent));
		this.shellNodeBase.doReparent();
		final ShellNodeParentBase newAbstractShellNodeParent = mock(ShellNodeParentBase.class);
		final ReparentListener reparentListener = spy(new ReparentListener());
		this.shellNodeBase.register(reparentListener);

		//when
		//the abstract shell node is reparented
		this.shellNodeBase.setParent(Optional.<ShellNodeParent>of(newAbstractShellNodeParent));
		this.shellNodeBase.doReparent();

		//then
		//the abstract shell node's parent is updated
		//the old parent is notified
		//the new parent is notified
		//the reparent listener is notified
		assertEquals(newAbstractShellNodeParent,
					 this.shellNodeBase.getParent().get());
		verify(oldAbstractShellNodeParent).handleChildReparent(this.shellNodeBase);
		verify(newAbstractShellNodeParent).handleChildReparent(this.shellNodeBase);
		//TODO verify event contents
		verify(reparentListener).handle((ShellNodeReparentedEvent) any());
	}

	@Test
	public void testDoResize() {
		//given
		//an abstract shell node with a geometry delegate
		//a resize listener
		final ShellNodeGeometryDelegate shellNodeGeometryDelegate = mock(ShellNodeGeometryDelegate.class);
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return shellNodeGeometryDelegate;
			}
		};
		final ResizeListener resizeListener = spy(new ResizeListener());
		this.shellNodeBase.register(resizeListener);

		//when
		//the abstract shell node is resized
		this.shellNodeBase.setSize(100,
									   200);
		this.shellNodeBase.doResize();

		//then
		//the abstract shell node's size is updated
		//the geometry delegate is notified
		//the resize listener is notified
		assertEquals(new Dimension(100,
								   200),
					 this.shellNodeBase.getSize());
		verify(shellNodeGeometryDelegate).resize(eq(new Dimension(100,
																  200)));
		//TODO verify event contents
		verify(resizeListener).handle((ShellNodeResizedEvent) any());
	}

	@Test
	public void testDoShow() {
		//given
		//an abstract shell node
		//a show listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final ShowListener showListener = spy(new ShowListener());
		this.shellNodeBase.register(showListener);

		//when
		//the abstract shell node is made visible
		this.shellNodeBase.doShow();

		//then
		//the abstract shell node is updated
		//the show listener is notified
		assertTrue(this.shellNodeBase.isVisible());
		//TODO verify event contents
		verify(showListener).handle((ShellNodeShowedEvent) any());
	}

	@Test
	public void testRequestHide() {
		//given
		//an abstract shell node
		//a request hide listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestHideListener requestHideListener = spy(new RequestHideListener());
		this.shellNodeBase.register(requestHideListener);

		//when
		//the abstract shell node is requested to be made invisible
		this.shellNodeBase.requestHide();

		//then
		//the request hide listener is notified
		//TODO verify event contents
		verify(requestHideListener).handle((ShellNodeHideRequestEvent) any());
	}

	@Test
	public void testRequestLower() {
		//given
		//an abstract shell node
		//a request lower listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestLowerListener requestLowerListener = spy(new RequestLowerListener());
		this.shellNodeBase.register(requestLowerListener);

		//when
		//the abstract shell node is requested to be lowered
		this.shellNodeBase.requestLower();

		//then
		//the request lower listener is notified
		//TODO verify event contents
		verify(requestLowerListener).handle((ShellNodeLowerRequestEvent) any());
	}

	@Test
	public void testRequestMove() {
		//given
		//an abstract shell node
		//a request move listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestMoveListener requestMoveListener = spy(new RequestMoveListener());
		this.shellNodeBase.register(requestMoveListener);

		//when
		//the abstract shell node is requested to be moved
		this.shellNodeBase.requestMove();

		//then
		//the request move listener is notified
		//TODO verify event contents
		verify(requestMoveListener).handle((ShellNodeMoveRequestEvent) any());
	}

	@Test
	public void testRequestRaise() {
		//given
		//an abstract shell node
		//a request raise listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestRaiseListener requestRaiseListener = spy(new RequestRaiseListener());
		this.shellNodeBase.register(requestRaiseListener);

		//when
		//the abstract shell node is requested to be raised
		this.shellNodeBase.requestRaise();

		//then
		//the request raise listener is notified
		//TODO verify event contents
		verify(requestRaiseListener).handle((ShellNodeRaiseRequestEvent) any());
	}

	@Test
	public void testRequestReparent(){
		//given
		//an abstract shell node
		//a request reparent listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestReparentListener requestReparentListener = spy(new RequestReparentListener());
		this.shellNodeBase.register(requestReparentListener);

		//when
		//the abstract shell node is requested to be reparented
		this.shellNodeBase.requestReparent();

		//then
		//the request reparent listener is notified
		//TODO verify event contents
		verify(requestReparentListener).handle((ShellNodeReparentRequestEvent) any());
	}

	@Test
	public void testRequestResize(){
		//given
		//an abstract shell node
		//a request resize listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestResizeListener requestResizeListener = spy(new RequestResizeListener());
		this.shellNodeBase.register(requestResizeListener);

		//when
		//the abstract shell node is requested to be resized
		this.shellNodeBase.requestResize();

		//then
		//the request resize listener is notified
		//TODO verify event contents
		verify(requestResizeListener).handle((ShellNodeResizeRequestEvent) any());
	}

	@Test
	public void testRequestShow(){
		//given
		//an abstract shell node
		//a request show listener
		this.shellNodeBase = new ShellNodeBase(this.shellScene ) {
			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}
		};
		final RequestShowListener requestShowListener = spy(new RequestShowListener());
		this.shellNodeBase.register(requestShowListener);

		//when
		//the abstract shell node is requested to be made visible
		this.shellNodeBase.requestShow();

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
