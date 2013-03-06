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

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplaySurfaceHandle;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QWidget;

@ThreadSafe
public class QJDisplaySurfaceHandle implements DisplaySurfaceHandle {

	private final WeakReference<QWidget> visualReference;
	private volatile boolean visualDestroyed = false;

	public QJDisplaySurfaceHandle(final QWidget visual) {
		this.visualReference = new WeakReference<QWidget>(visual);
		installDestroyListener();
	}

	private void installDestroyListener() {
		final Runnable destroyListener = new Runnable() {
			@Override
			public void run() {
				final QWidget visual = QJDisplaySurfaceHandle.this.visualReference.get();
				if (visual != null) {
					visual.installEventFilter(new QObject() {
						@Override
						public boolean eventFilter(	final QObject qObject,
													final QEvent qEvent) {
							if ((qEvent.type() == QEvent.Type.Destroy) && qObject.equals(visual)) {
								QJDisplaySurfaceHandle.this.visualDestroyed = true;
							}
							return false;
						}
					});
				}
			}
		};
		QCoreApplication.invokeLater(destroyListener);

	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DisplaySurfaceHandle) {
			final DisplaySurfaceHandle otherObj = (DisplaySurfaceHandle) obj;
			return otherObj.getNativeHandle().equals(getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getNativeHandle().hashCode();
	}

	@Override
	public Integer getNativeHandle() {
		final FutureTask<Integer> getHandleTask = new FutureTask<Integer>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				final QWidget visual = QJDisplaySurfaceHandle.this.visualReference.get();
				int visualId = 0;
				if ((visual != null) && !QJDisplaySurfaceHandle.this.visualDestroyed) {
					visualId = (int) visual.effectiveWinId();
				}
				return Integer.valueOf(visualId);
			}
		});
		QCoreApplication.invokeLater(getHandleTask);
		Integer handle = null;
		try {
			handle = getHandleTask.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return handle;
	}
}