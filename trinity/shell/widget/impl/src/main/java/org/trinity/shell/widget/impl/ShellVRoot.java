/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.widget.impl;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellSurface;
import org.trinity.shell.geo.api.ShellNodeExecutor;
import org.trinity.shell.widget.api.view.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
// TODO list events that are emitted by this widget in doc
/**
 * A <code>ShellVRoot</code> is a <code>ShellWidget</code> that can be used as a
 * virtual desktop for a <code>ManagedDisplay</code>. Unlike the
 * <code>RealRoot<code>, there can be more than one <code>ShellVRoot</code>,
 * each referencing a different unique native window.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(@Named("shellVRoot"))
public final class ShellVRoot extends ShellWidgetImpl {

	@Inject
	protected ShellVRoot(	final EventBus eventBus,
							final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
							final PainterFactory painterFactory,
							@Named("ShellWidget") final ShellNodeExecutor shellNodeExecutor,
							@Named("shellRootRenderArea") final ShellSurface root,
							final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellNodeExecutor,
				view);
		setParent(root);
		setX(root.getX());
		setY(root.getY());
		setWidth(root.getWidth());
		setHeight(root.getHeight());
		requestReparent();
		requestMoveResize();
	}
}
