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
package org.trinity.foundation.render.qt.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.AsyncListenable;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

@Bind(to = @To(IMPLEMENTATION))
@Singleton
@NotThreadSafe
public class RenderEventConverter {

	private static final Logger LOG = LoggerFactory.getLogger(RenderEventConverter.class);

	private final Map<QEvent.Type, Optional<RenderEventConversion>> conversionByQEventType = new HashMap<QEvent.Type, Optional<RenderEventConversion>>();

	@Inject
	RenderEventConverter(final Set<RenderEventConversion> eventConversions) {
		for (final RenderEventConversion eventConverter : eventConversions) {
			this.conversionByQEventType.put(eventConverter.getQEventType(),
											Optional.of(eventConverter));
		}
	}

	public boolean convertRenderEvent(	final AsyncListenable eventTarget,
										final Object view,
										final QObject eventProducer,
										final QEvent event) {

		final Optional<RenderEventConversion> eventConverter = this.conversionByQEventType.get(event.type());
		DisplayEvent displayEvent = null;
		if (eventConverter.isPresent()) {
			displayEvent = eventConverter.get().convertEvent(	view,
																eventProducer,
																event);

			LOG.debug("Converted render event={} to display event={}.",
					event,
					displayEvent);

			eventTarget.post(displayEvent);
			return true;
		}

		return false;
	}
}