package org.trinity.shellplugin.widget.impl.view.qt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.shell.api.plugin.ShellPlugin;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.trolltech.qt.gui.QApplication;

import javax.inject.Singleton;

@Bind(multiple = true)
@Singleton
public class StyledViewsPlugin extends AbstractIdleService implements ShellPlugin {

	private static final String STYLESHEET_NAME = "views.qss";

	StyledViewsPlugin() {
	}

	private void loadStylelSheet() throws IOException {
		final InputStream in = getClass().getClassLoader().getResourceAsStream(STYLESHEET_NAME);
		final String content = CharStreams.toString(new InputStreamReader(	in,
																			Charsets.UTF_8));
		QApplication.instance().setStyleSheet(content);
	}

	@Override
	protected void startUp() throws Exception {
		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(new Callable<Void>() {
			@Override
			public Void call() {
				try {
					loadStylelSheet();
				} catch (final IOException e) {
					Throwables.propagate(e);
				}
				return null;
			}
		});
		QApplication.invokeLater(futureTask);
	}

	@Override
	protected void shutDown() throws Exception {
		// clear styles?

	}
}
