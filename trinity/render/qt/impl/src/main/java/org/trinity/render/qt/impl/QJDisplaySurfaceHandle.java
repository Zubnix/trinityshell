package org.trinity.render.qt.impl;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.trinity.foundation.display.api.DisplaySurfaceHandle;

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QWidget;

public class QJDisplaySurfaceHandle implements DisplaySurfaceHandle {

	private final WeakReference<QWidget> visualReference;
	private volatile boolean visualDestroyed = false;

	QJDisplaySurfaceHandle(final QWidget visual) {
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