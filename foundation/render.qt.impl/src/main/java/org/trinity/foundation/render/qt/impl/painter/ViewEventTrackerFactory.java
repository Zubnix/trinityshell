package org.trinity.foundation.render.qt.impl.painter;

import org.trinity.foundation.api.shared.AsyncListenable;

import com.trolltech.qt.core.QObject;

public interface ViewEventTrackerFactory {
	QObject createQJEventTracker(	final AsyncListenable target,
									final QObject view);
}
