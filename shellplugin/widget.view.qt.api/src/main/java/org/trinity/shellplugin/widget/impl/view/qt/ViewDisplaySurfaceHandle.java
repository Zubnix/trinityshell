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

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QWidget;

@ThreadSafe
public class ViewDisplaySurfaceHandle implements DisplaySurfaceHandle {

	private static final Logger LOG = LoggerFactory.getLogger(ViewDisplaySurfaceHandle.class);
	private final WeakReference<QWidget> visualReference;
	private volatile boolean visualDestroyed = false;

	ViewDisplaySurfaceHandle(@Nonnull final QWidget visual) {
		this.visualReference = new WeakReference<QWidget>(checkNotNull(visual));
		installDestroyListener();
	}

	private void installDestroyListener() {
		final Runnable destroyListener = new Runnable() {
			@Override
			public void run() {
				final QWidget visual = ViewDisplaySurfaceHandle.this.visualReference.get();
				if (visual != null) {
					visual.installEventFilter(new QObject() {
						@Override
						public boolean eventFilter(	final QObject qObject,
													final QEvent qEvent) {
							if ((qEvent.type() == QEvent.Type.Destroy) && qObject.equals(visual)) {
								ViewDisplaySurfaceHandle.this.visualDestroyed = true;
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

//		final FutureTask<Integer> getHandleTask = new FutureTask<Integer>(new Callable<Integer>() {
//			@Override
//			public Integer call() {
				final QWidget visual = ViewDisplaySurfaceHandle.this.visualReference.get();
				int visualId = 0;
				if ((visual != null) && !ViewDisplaySurfaceHandle.this.visualDestroyed) {
					visualId = (int) visual.effectiveWinId();
				}
				return Integer.valueOf(visualId);
//			}
//		});
//
//		QCoreApplication.invokeLater(getHandleTask);
//		Integer handle = null;
//		try {
//			handle = getHandleTask.get();
//		} catch (final InterruptedException e) {
//			LOG.error(	"Interrupted while while waiting for native render handle",
//						e);
//		} catch (final ExecutionException e) {
//			LOG.error(	"Exception while querying native render handle",
//						e);
//		}
//		return handle;
	}
}
