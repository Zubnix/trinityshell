/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.render.qt.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.gui.QApplication;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

@Bind(to = @To(value = To.Type.IMPLEMENTATION))
@ThreadSafe
public class RenderApplicationStarter implements Runnable {

	private final ListeningExecutorService qRenderEventPump = MoreExecutors.listeningDecorator(Executors
			.newSingleThreadExecutor());

	public ListenableFuture<Void> start() {
		return this.qRenderEventPump.submit(this,
											null);
	}

	public ListenableFuture<Void> stop() {

		final Callable<Void> updateChildViewPositionRoutine = new Callable<Void>() {
			@Override
			public Void call() throws ExecutionException {
				QCoreApplication.quit();
				return null;
			}
		};
		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(updateChildViewPositionRoutine);

		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public void run() {
		QApplication.initialize(new String[] {});
		QApplication.setQuitOnLastWindowClosed(false);
		final int r = QApplication.exec();
		if (r != 0) {
			throw new RuntimeException("Qt Jambi exited with error: " + r);
		}
	}
}