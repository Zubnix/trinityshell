package org.trinity.shellplugin.wm.impl;

import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceParent;
import org.trinity.shell.api.widget.ShellWidget;
import org.trinity.shellplugin.widget.api.ShellWidgetBar;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

// @Bind
@To(value = Type.IMPLEMENTATION)
@Singleton
public class DesktopImpl {

	@Inject
	@Named("ShellRootSurface")
	private ShellSurfaceParent shellRootSurface;

	@Inject
	private ShellWidget shellWidgetRoot;
	@Inject
	private ShellLayoutManagerLine shellLayoutManagerLineRoot;
	@Inject
	private ShellWidgetBar shellWidgetBarTop;
	@Inject
	@Named("ShellVirtualNode")
	private ShellNodeParent shellNodeParentClient;
	@Inject
	private ShellLayoutManagerLine shellLayoutManagerLineClient;
	@Inject
	private ShellWidgetBar shellWidgetBarBottom;

	public void setup() {
		// this.shellWidgetRoot.construct();
		final int rootWidth = this.shellRootSurface.getWidth();
		final int rootHeight = this.shellRootSurface.getHeight();
		this.shellWidgetRoot.setWidth(rootWidth);
		this.shellWidgetRoot.setHeight(rootHeight);
		this.shellWidgetRoot.doResize();

		this.shellLayoutManagerLineRoot.setHorizontalDirection(false);
		this.shellLayoutManagerLineRoot.setInverseDirection(false);
		this.shellWidgetRoot.setLayoutManager(this.shellLayoutManagerLineRoot);

		// setup topbar, client display area and bottombar.
		// topbar
		this.shellWidgetBarTop.setHeight(25);
		this.shellWidgetBarTop.doResize();
		final ShellLayoutPropertyLine layoutPropertyTopBar = new ShellLayoutPropertyLine(	0,
																							Margins.NO_MARGINS);
		this.shellLayoutManagerLineRoot.addChildNode(	this.shellWidgetBarTop,
														layoutPropertyTopBar);
		this.shellWidgetBarTop.setParent(this.shellWidgetRoot);
		this.shellWidgetBarTop.doReparent();

		// client display area
		final ShellLayoutPropertyLine layoutPropertyClientDisplayArea = new ShellLayoutPropertyLine(1,
																									new Margins(5));
		this.shellLayoutManagerLineRoot.addChildNode(	this.shellNodeParentClient,
														layoutPropertyClientDisplayArea);
		this.shellNodeParentClient.setParent(this.shellWidgetRoot);
		this.shellNodeParentClient.doReparent();

		this.shellNodeParentClient.setLayoutManager(this.shellLayoutManagerLineClient);

		// bottombar
		this.shellWidgetBarBottom.setHeight(25);
		this.shellWidgetBarBottom.doResize();
		final ShellLayoutPropertyLine layoutPropertyBottomBar = new ShellLayoutPropertyLine(0,
																							Margins.NO_MARGINS);
		this.shellLayoutManagerLineRoot.addChildNode(	this.shellWidgetBarBottom,
														layoutPropertyBottomBar);
		this.shellWidgetBarBottom.setParent(this.shellWidgetRoot);
		this.shellWidgetBarBottom.doReparent();

		this.shellWidgetRoot.layout();

		this.shellWidgetBarTop.doShow();
		this.shellNodeParentClient.doShow();
		this.shellWidgetBarBottom.doShow();
		this.shellWidgetRoot.doShow();
	}

	public void tearDown() {
		this.shellWidgetRoot.doHide();
		this.shellWidgetRoot.doDestroy();
	}

	public void addClient(final ShellSurface shellSurfaceClient) {
		this.shellLayoutManagerLineClient.addChildNode(	shellSurfaceClient,
														new ShellLayoutPropertyLine(1,
																					new Margins(2)));
		shellSurfaceClient.setParent(this.shellNodeParentClient);
		shellSurfaceClient.doReparent();

		this.shellNodeParentClient.layout();
	}
}
