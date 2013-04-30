package org.trinity.shellplugin.wm.x11.impl.scene;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shell.api.surface.ShellSurfaceParent;
import org.trinity.shell.api.widget.BaseShellWidget;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;

public class ShellRootWidget extends BaseShellWidget {

	private final EventList<Object> topBar = new BasicEventList<Object>();
	private final EventList<Object> bottomBar = new BasicEventList<Object>();

	@Inject
	protected ShellRootWidget(	final ShellSurfaceFactory shellSurfaceFactory,
								@Named("Shell") final ListeningExecutorService shellExecutor,
								final PainterFactory painterFactory,
								@Named("RootView") final Object view) {
		super(	shellSurfaceFactory,
				shellExecutor,
				painterFactory,
				view);
		getPainter().bindView();

		// find correct size
		final ListenableFuture<Rectangle> rootGeometryFuture = transform(	shellSurfaceFactory.getRootShellSurface(),
																			new AsyncFunction<ShellSurfaceParent, Rectangle>() {
																				@Override
																				public ListenableFuture<Rectangle> apply(final ShellSurfaceParent input)
																						throws Exception {
																					return input.getGeometry();
																				}
																			});
		// set correct size
		addCallback(rootGeometryFuture,
					new FutureCallback<Rectangle>() {
						@Override
						public void onSuccess(final Rectangle result) {
							setSize(result.getSize());
							doResize();
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub
							t.printStackTrace();
						}
					},
					shellExecutor);
	}

	public EventList<Object> getTopBar() {
		return this.topBar;
	}

	public EventList<Object> getBottomBar() {
		return this.bottomBar;
	}
}