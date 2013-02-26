package org.trinity.shellplugin.widget.impl.view.qt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.shell.api.plugin.ShellPlugin;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QApplication;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class StyledViewsPlugin implements ShellPlugin {

	private static final String STYLESHEET_NAME = "views.qss";

	private final PaintRenderer paintRenderer;

	@Inject
	StyledViewsPlugin(final PaintRenderer paintRenderer) {
		this.paintRenderer = paintRenderer;
	}

	@Override
	public void start() {
		paintRenderer.invoke(	this,
								new PaintRoutine<Void, PaintContext>() {
									@Override
									public Void call(final PaintContext paintContext) {
										try {
											loadStylelSheet();
										} catch (final IOException e) {
											Throwables.propagate(e);
										}
										return null;
									}
								});
	}

	private void loadStylelSheet() throws IOException {
		final InputStream in = getClass().getClassLoader().getResourceAsStream(STYLESHEET_NAME);
		final String content = CharStreams.toString(new InputStreamReader(	in,
																			Charsets.UTF_8));
		QApplication.instance().setStyleSheet(content);
	}

	@Override
	public void stop() {
		// clear styles?
	}
}