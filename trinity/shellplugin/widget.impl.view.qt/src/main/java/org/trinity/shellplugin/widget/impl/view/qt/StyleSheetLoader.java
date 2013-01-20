package org.trinity.shellplugin.widget.impl.view.qt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.PaintRoutine;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QApplication;

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
		QApplication.instance().setStyleSheet(content);
	}
}