package org.trinity.shellplugin.wm.x11.impl.scene;

import static com.google.common.util.concurrent.Futures.addCallback;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.shell.api.bindingkey.ShellRootNode;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shellplugin.wm.api.Desktop;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Bind
@ShellRootNode
public class ShellRootWidget extends BaseShellWidget implements Desktop {

	private final EventList<Object> notificationsBar = new BasicEventList<Object>();
	private final EventList<Object> clientsBar = new BasicEventList<Object>();
	private final EventList<Object> bottomBar = new BasicEventList<Object>();

	@Inject
	protected ShellRootWidget(	@ShellScene final AsyncListenable shellScene,
								@ShellExecutor final ListeningExecutorService shellExecutor,
								final PainterFactory painterFactory,
								@Named("RootView") final Object view) {
		super(	shellScene,
				shellExecutor,
				painterFactory,
				view);
		getPainter().bindView();

		// find correct size
		final ListenableFuture<Rectangle> rootGeometryFuture = shellScene.getRootShellNodeParent().getGeometry();
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

	@Override
	public EventList<Object> getNotificationsBar() {
		return this.notificationsBar;
	}

	@Override
	public EventList<Object> getClientsBar() {
		return this.clientsBar;
	}

	@Override
	public EventList<Object> getBottomBar() {
		return this.bottomBar;
	}
}
