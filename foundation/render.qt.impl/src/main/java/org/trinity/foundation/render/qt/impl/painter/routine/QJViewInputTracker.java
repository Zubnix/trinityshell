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
package org.trinity.foundation.render.qt.impl.painter.routine;

import java.util.List;

import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.render.qt.impl.QJRenderEventConverter;

import com.trolltech.qt.core.QChildEvent;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;

public class QJViewInputTracker extends QObject {

	private final AsyncListenable eventTarget;
	private final QJRenderEventConverter renderEventConverter;
	private final QObject view;

	QJViewInputTracker(	final QJRenderEventConverter qjRenderEventConverter,
						final AsyncListenable eventTarget,
						final QObject view) {
		this.renderEventConverter = qjRenderEventConverter;
		this.eventTarget = eventTarget;
		this.view = view;
		trackQObjectTree(view);
	}

	private void trackQObjectTree(final QObject root) {
		root.installEventFilter(this);

		final List<QObject> childList = root.children();
		for (final QObject child : childList) {
			trackQObjectTree(child);
		}
	}

	private void untrackQObjectTree(final QObject root) {
		root.removeEventFilter(this);

		final List<QObject> childList = root.children();
		for (final QObject child : childList) {
			untrackQObjectTree(child);
		}
	}

	@Override
	public boolean eventFilter(	final QObject eventProducer,
								final QEvent qEvent) {

		boolean eventConsumed = false;

		switch (qEvent.type()) {
			case ChildAdded: {
				final QChildEvent childEvent = (QChildEvent) qEvent;
				final QObject child = childEvent.child();
				trackQObjectTree(child);
				break;
			}
			case ChildRemoved: {
				final QChildEvent childEvent = (QChildEvent) qEvent;
				final QObject child = childEvent.child();
				untrackQObjectTree(child);
				break;
			}
			case KeyPress: {
				eventConsumed = this.renderEventConverter.convertRenderEvent(	this.eventTarget,
																				this.view,
																				eventProducer,
																				qEvent);
				break;
			}
			case KeyRelease: {
				eventConsumed = this.renderEventConverter.convertRenderEvent(	this.eventTarget,
																				this.view,
																				eventProducer,
																				qEvent);
				break;
			}
			case MouseButtonPress: {
				eventConsumed = this.renderEventConverter.convertRenderEvent(	this.eventTarget,
																				this.view,
																				eventProducer,
																				qEvent);
				break;
			}
			case MouseButtonRelease: {
				eventConsumed = this.renderEventConverter.convertRenderEvent(	this.eventTarget,
																				this.view,
																				eventProducer,
																				qEvent);
				break;
			}
			default:
				break;
		}
		return eventConsumed;
	}
}
