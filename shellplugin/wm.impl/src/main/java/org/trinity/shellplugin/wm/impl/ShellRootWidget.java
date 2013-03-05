package org.trinity.shellplugin.wm.impl;

import java.util.Set;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ShellRootWidget extends BaseShellWidget {

	@Inject
	@Named("RootView")
	private Object view;

	private EventList<Object> topBar = new BasicEventList<Object>();
	private EventList<Object> bottomBar = new BasicEventList<Object>();

	@Inject
	protected ShellRootWidget(	final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory) {
		super(	shellDisplayEventDispatcher,
				painterFactory);
	}

	@ViewReference
	public Object getView() {
		return this.view;
	}

	public EventList<Object> getTopBar() {
		return topBar;
	}

	@Named("TopBarItem")
	public void addTopBarItems(Set<?> topBarItems) {
		try {
			topBar.getReadWriteLock().writeLock().lock();
			topBar.addAll(topBarItems);
		} finally {
			topBar.getReadWriteLock().writeLock().unlock();
		}
	}

	public EventList<Object> getBottomBar() {
		return bottomBar;
	}

	@Named("BottomBarItem")
	public void addBottomBarItems(Set<?> bottomBarItems) {
		try {
			bottomBar.getReadWriteLock().writeLock().lock();
			bottomBar.addAll(bottomBarItems);
		} finally {
			bottomBar.getReadWriteLock().writeLock().unlock();
		}
	}
}