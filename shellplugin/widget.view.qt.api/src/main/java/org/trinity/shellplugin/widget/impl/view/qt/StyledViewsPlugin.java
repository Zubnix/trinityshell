/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shellplugin.widget.impl.view.qt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.shell.api.plugin.ShellPlugin;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.trolltech.qt.gui.QApplication;

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
