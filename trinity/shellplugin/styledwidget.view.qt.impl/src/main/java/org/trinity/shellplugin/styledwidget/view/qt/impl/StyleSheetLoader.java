package org.trinity.shellplugin.styledwidget.view.qt.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.PaintRenderer;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class StyleSheetLoader {

	private static final String STYLESHEET_NAME = "views.qss";

	@Inject
	StyleSheetLoader(final PaintRenderer renderEngine) {
		renderEngine.invoke(this,
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

	}
}