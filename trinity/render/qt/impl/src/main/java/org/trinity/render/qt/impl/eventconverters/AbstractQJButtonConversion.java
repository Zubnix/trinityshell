package org.trinity.render.qt.impl.eventconverters;

import java.util.concurrent.ExecutionException;

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.PointerInput;
import org.trinity.render.qt.impl.QJRenderEventConversion;
import org.trinity.shellplugin.widget.api.binding.BindingDiscovery;
import org.trinity.shellplugin.widget.api.binding.BoundButtonInputEvent;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QMouseEvent;

public abstract class AbstractQJButtonConversion implements QJRenderEventConversion {

	private final BindingDiscovery bindingDiscovery;

	AbstractQJButtonConversion(BindingDiscovery bindingDiscovery) {
		this.bindingDiscovery = bindingDiscovery;
	}

	@Override
	public DisplayEvent convertEvent(	DisplayEventSource source,
										Object view,
										QObject eventProducer,
										QEvent qEvent) {
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
			inputSlotName = bindingDiscovery.lookupInputEmitterInputSlotName(	view,
																				eventProducer,
																				PointerInput.class);
		} catch (ExecutionException e) {
			Throwables.propagate(e);
			inputSlotName = Optional.absent();
		}

		ButtonNotifyEvent buttonNotifyEvent;
		if (inputSlotName.isPresent()) {
			buttonNotifyEvent = new BoundButtonInputEvent(	source,
															pointerInput,
															inputSlotName.get());
		} else {
			buttonNotifyEvent = new ButtonNotifyEvent(	source,
														pointerInput);
		}
		return buttonNotifyEvent;
	}

	public abstract Momentum getMomentum();
}
