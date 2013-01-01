package org.trinity.foundation.render.qt.impl.eventconverters;

import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.event.ButtonNotifyEvent;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.BindingDiscovery;
import org.trinity.foundation.api.render.binding.refactor.view.BoundButtonInputEvent;
import org.trinity.foundation.render.qt.impl.QJRenderEventConversion;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QMouseEvent;

public abstract class AbstractQJButtonConversion implements QJRenderEventConversion {

	private final BindingDiscovery bindingDiscovery;

	AbstractQJButtonConversion(final BindingDiscovery bindingDiscovery) {
		this.bindingDiscovery = bindingDiscovery;
	}

	@Override
	public DisplayEvent convertEvent(	final Object eventTarget,
										final Object view,
										final QObject eventProducer,
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
		final PointerInput pointerInput = new PointerInput(	getMomentum(),
															button,
															inputModifiers,
															relativeX,
															relativeY,
															rootX,
															rootY);

		Optional<String> inputSlotName;
		try {
			inputSlotName = this.bindingDiscovery.lookupInputEmitterInputSlotName(	view,
																					eventProducer,
																					PointerInput.class);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
			inputSlotName = Optional.absent();
		}

		ButtonNotifyEvent buttonNotifyEvent;
		if (inputSlotName.isPresent()) {
			buttonNotifyEvent = new BoundButtonInputEvent(	eventTarget,
															pointerInput,
															inputSlotName.get());
		} else {
			buttonNotifyEvent = new ButtonNotifyEvent(	eventTarget,
														pointerInput);
		}
		return buttonNotifyEvent;
	}

	public abstract Momentum getMomentum();
}
