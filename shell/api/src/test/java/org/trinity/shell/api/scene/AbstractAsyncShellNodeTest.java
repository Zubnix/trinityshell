package org.trinity.shell.api.scene;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.api.shared.Size;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AbstractAsyncShellNodeTest {

	@Mock
	private ListeningExecutorService shellExecutor;

	@Test
	public void testAsyncDelegations() {
		//given
		//an abstract async shellnode with empty implementations
		final AbstractAsyncShellNode abstractAsyncShellNode = new AbstractAsyncShellNode(this.shellExecutor) {
			@Override
			public Coordinate getPositionImpl() {
				return null;
			}

			@Override
			public Size getSizeImpl() {
				return null;
			}

			@Override
			public Void setPositionImpl(final Coordinate position) {
				return null;
			}

			@Override
			public Void setSizeImpl(final Size size) {
				return null;
			}

			@Override
			public Rectangle getGeometryImpl() {
				return null;
			}

			@Override
			public Boolean isVisibleImpl() {
				return null;
			}

			@Override
			public Void cancelPendingMoveImpl() {
				return null;
			}

			@Override
			public Void cancelPendingResizeImpl() {
				return null;
			}

			@Override
			public Void doDestroyImpl() {
				return null;
			}

			@Override
			public Void doLowerImpl() {
				return null;
			}

			@Override
			public Void doReparentImpl() {
				return null;
			}

			@Override
			public Void doMoveImpl() {
				return null;
			}

			@Override
			public Void doRaiseImpl() {
				return null;
			}

			@Override
			public Void doMoveResizeImpl() {
				return null;
			}

			@Override
			public Void doResizeImpl() {
				return null;
			}

			@Override
			public Void doShowImpl() {
				return null;
			}

			@Override
			public Void doHideImpl() {
				return null;
			}

			@Override
			public Boolean isDestroyedImpl() {
				return null;
			}

			@Override
			public Void requestLowerImpl() {
				return null;
			}

			@Override
			public Void requestMoveImpl() {
				return null;
			}

			@Override
			public Void requestMoveResizeImpl() {
				return null;
			}

			@Override
			public Void requestRaiseImpl() {
				return null;
			}

			@Override
			public Void requestReparentImpl() {
				return null;
			}

			@Override
			public Void requestResizeImpl() {
				return null;
			}

			@Override
			public Void requestShowImpl() {
				return null;
			}

			@Override
			public Void requestHideImpl() {
				return null;
			}

			@Override
			public Void setSizeImpl(final int width,
									final int height) {
				return null;
			}

			@Override
			public Void setPositionImpl(final int x,
										final int y) {
				return null;
			}

			@Override
			public Void setParentImpl(final Optional<? extends ShellNodeParent> parent) {
				return null;
			}

			@Override
			public Optional<ShellNodeParent> getParentImpl() {
				return null;
			}

			@Override
			public ShellNodeTransformation toGeoTransformationImpl() {
				return null;
			}

			@Override
			public ShellNodeGeometryDelegate getShellNodeGeometryDelegate() {
				return null;
			}

			@Override
			public void register(@Nonnull final Object listener) {

			}

			@Override
			public void register(@Nonnull final Object listener,
								 @Nonnull final ExecutorService executor) {

			}

			@Override
			public void unregister(@Nonnull final Object listener) {

			}

			@Override
			public void post(@Nonnull final Object event) {

			}

			@Override
			public void scheduleRegister(@Nonnull final Object listener) {

			}

			@Override
			public void scheduleRegister(@Nonnull final Object listener,
										 @Nonnull final ExecutorService listenerActivationExecutor) {

			}
		};

		//when
		//any of it's async methods is called
		abstractAsyncShellNode.cancelPendingMove();//1
		abstractAsyncShellNode.cancelPendingResize();//2
		abstractAsyncShellNode.doDestroy();//3
		abstractAsyncShellNode.doHide();//4
		abstractAsyncShellNode.doLower();//5
		abstractAsyncShellNode.doMove();//6
		abstractAsyncShellNode.doMoveResize();//7
		abstractAsyncShellNode.doRaise();//8
		abstractAsyncShellNode.doReparent();//9
		abstractAsyncShellNode.doResize();//10
		abstractAsyncShellNode.doShow();//11
		abstractAsyncShellNode.getGeometry();//12
		abstractAsyncShellNode.getParent();//13
		abstractAsyncShellNode.getPosition();//14
		abstractAsyncShellNode.getSize();//15
		abstractAsyncShellNode.isDestroyed();//16
		abstractAsyncShellNode.isVisible();//17
		abstractAsyncShellNode.requestHide();//18
		abstractAsyncShellNode.requestLower();//19
		abstractAsyncShellNode.requestMove();//20
		abstractAsyncShellNode.requestMoveResize();//21
		abstractAsyncShellNode.requestRaise();//22
		abstractAsyncShellNode.requestReparent();//23
		abstractAsyncShellNode.requestResize();//24
		abstractAsyncShellNode.requestShow();//25
		abstractAsyncShellNode.setParent(Optional.<ShellNodeParent>absent());//26
		abstractAsyncShellNode.setPosition(new Coordinate(1,
														  2));//27
		abstractAsyncShellNode.setPosition(1,
										   2);//28
		abstractAsyncShellNode.setSize(10,
									   20);//29
		abstractAsyncShellNode.setSize(new Size(10,
												20));//30
		abstractAsyncShellNode.toGeoTransformation();//31

		//then
		//the call is delegated to it's executor.
		verify(this.shellExecutor,
			   times(31)).submit((Callable<Object>) any());
	}
}
