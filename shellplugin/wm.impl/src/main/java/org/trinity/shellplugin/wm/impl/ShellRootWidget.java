package org.trinity.shellplugin.wm.impl;

import java.util.Set;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shell.api.widget.BaseShellWidget;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ShellRootWidget extends BaseShellWidget {

	@Inject
	@Named("RootView")
	private Object view;

	private final EventList<Object> topBar = new BasicEventList<Object>();
	private final EventList<Object> bottomBar = new BasicEventList<Object>();

	@Inject
	protected ShellRootWidget(	final ShellSurfaceFactory shellSurfaceFactory,
								@Named("ShellExecutor") final ListeningExecutorService shellExecutor,
								final PainterFactory painterFactory) {
		super(	shellSurfaceFactory,
				shellExecutor,
				painterFactory);
	}

	@ViewReference
	public Object getView() {
		return this.view;
	}

	public EventList<Object> getTopBar() {
		return this.topBar;
	}

	@Inject
	@Named("TopBarItem")
	public void addTopBarItems(final Set<?> topBarItems) {
		try {
			this.topBar.getReadWriteLock().writeLock().lock();
			this.topBar.addAll(topBarItems);
		} finally {
			this.topBar.getReadWriteLock().writeLock().unlock();
		}
	}

	public EventList<Object> getBottomBar() {
		return this.bottomBar;
	}

	@Inject
	@Named("BottomBarItem")
	public void addBottomBarItems(final Set<?> bottomBarItems) {
		try {
			this.bottomBar.getReadWriteLock().writeLock().lock();
			this.bottomBar.addAll(bottomBarItems);
		} finally {
			this.bottomBar.getReadWriteLock().writeLock().unlock();
		}
	}
}