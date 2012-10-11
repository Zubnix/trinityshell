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
package org.trinity.render.qt.impl.eventconverters;

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.PointerInput;
import org.trinity.render.qt.impl.QJRenderEventConversion;

import com.google.inject.Singleton;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.gui.QMouseEvent;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class QJButtonPressedConversion implements QJRenderEventConversion {

	QJButtonPressedConversion() {

	}

	@Override
	public ButtonNotifyEvent convertEvent(	final DisplayEventSource source,
											final QEvent qEvent) {
		qEvent.accept();

		final QMouseEvent qMouseEvent = (QMouseEvent) qEvent;

		final int buttonCode = qMouseEvent.button().value();
		final int state = qMouseEvent.modifiers().value();

		final int rootX = qMouseEvent.globalX();
		final int rootY = qMouseEvent.globalY();
		final int relativeX = qMouseEvent.x();
		final int relativeY = qMouseEvent.y();

		final Button button = new Button(buttonCode);
		final InputModifiers inputModifiers = new InputModifiers(state);
		final PointerInput mouseInput = new PointerInput(	Momentum.STARTED,
															button,
															inputModifiers,
															relativeX,
															relativeY,
															rootX,
															rootY);

		return new ButtonNotifyEvent(	source,
										mouseInput);
	}

	@Override
	public Type getQEventType() {
		return QEvent.Type.MouseButtonPress;
	}
}