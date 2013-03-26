package org.trinity.shellplugin.wm.impl;

import java.util.Set;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shellplugin.wm.api.BottomBarItem;
import org.trinity.shellplugin.wm.api.TopBarItem;

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
								final PainterFactory painterFactory,
								final Set<TopBarItem> topBarItems,
								final Set<BottomBarItem> bottomBarItems) {
		super(	shellSurfaceFactory,
				shellExecutor,
				painterFactory);
		addTopBarItems(topBarItems);
		addBottomBarItems(bottomBarItems);
	}

	@Override
	public Object getViewImpl() {
		return this.view;
	}

	public EventList<Object> getTopBar() {
		return this.topBar;
	}

	private void addTopBarItems(final Set<TopBarItem> topBarItems) {
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

	private void addBottomBarItems(final Set<BottomBarItem> bottomBarItems) {
		try {
			this.bottomBar.getReadWriteLock().writeLock().lock();
			this.bottomBar.addAll(bottomBarItems);
		} finally {
			this.bottomBar.getReadWriteLock().writeLock().unlock();
		}
	}
}