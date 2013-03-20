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

import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.render.qt.impl.QJRenderEventConverter;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QObject;

public class QJViewEventTracker extends QObject {

	private final AsyncListenable eventTarget;
	private final QJRenderEventConverter renderEventConverter;
	private final QObject view;

	QJViewEventTracker(	final QJRenderEventConverter qjRenderEventConverter,
						final AsyncListenable target,
						final QObject view) {
		this.renderEventConverter = qjRenderEventConverter;
		this.eventTarget = target;
		this.view = view;
		this.view.installEventFilter(this);
	}

	@Override
	public boolean eventFilter(	final QObject eventProducer,
								final QEvent qEvent) {

		boolean eventConsumed = false;
		if (isViewEvent(eventProducer,
						qEvent)) {

			eventConsumed = this.renderEventConverter.convertRenderEvent(	this.eventTarget,
																			this.view,
																			eventProducer,
																			qEvent);
		}
		return eventConsumed;
	}

	private boolean isViewEvent(final QObject eventProducer,
								final QEvent qEvent) {
		return (qEvent.type() == Type.Enter) || (qEvent.type() == Type.Leave) || (qEvent.type() == Type.FocusIn)
				|| (qEvent.type() == Type.FocusOut);
	}
}
