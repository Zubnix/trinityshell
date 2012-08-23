package org.trinity.render.qtlnf.impl;

import java.util.List;
import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.render.api.Painter;
import org.trinity.shell.widget.api.view.ShellKeyDrivenMenuView;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellKeyDrivenMenuViewImpl extends ShellWidgetViewImpl implements
		ShellKeyDrivenMenuView {

	private final DisplaySurfaceFactory displaySurfaceFactory;

	private Painter painter;

	@Inject
	ShellKeyDrivenMenuViewImpl(final DisplaySurfaceFactory displaySurfaceFactory) {
		super(displaySurfaceFactory);
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;
		return super.create(painter);
	}

	@Override
	public Future<Void> clear() {
		return null;
	}

	@Override
	public Future<Void> activate() {
		return null;
	}

	@Override
	public Future<Void> deactivate() {
		return null;
	}

	@Override
	public Future<Void> update(	final String input,
								final List<String> filteredChoices,
								final int activeChoiceIdx) {
		return null;
	}
}