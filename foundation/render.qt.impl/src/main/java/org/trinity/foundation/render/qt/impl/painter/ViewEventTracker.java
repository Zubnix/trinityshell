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
package org.trinity.foundation.render.qt.impl.painter;

import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.render.qt.impl.RenderEventConverter;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.core.QObject;

public class ViewEventTracker extends QObject {

	private final AsyncListenable eventTarget;
	private final RenderEventConverter renderEventConverter;
	private final QObject view;

	@AssistedInject
	ViewEventTracker(	final RenderEventConverter qjRenderEventConverter,
						@Assisted final AsyncListenable target,
						@Assisted final QObject view) {
		this.renderEventConverter = qjRenderEventConverter;
		this.eventTarget = target;
		this.view = view;
	}

	@Override
	public boolean eventFilter(	final QObject eventProducer,
								final QEvent qEvent) {

		boolean eventConsumed = false;
		if (isViewEvent(eventProducer,
						qEvent)) {
			if (qEvent.type() == Type.Destroy) {
				eventProducer.removeEventFilter(this);
			}
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
				|| (qEvent.type() == Type.FocusOut) || (qEvent.type() == Type.Destroy);
	}
}
