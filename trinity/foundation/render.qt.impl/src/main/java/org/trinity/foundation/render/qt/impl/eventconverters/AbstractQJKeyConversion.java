package org.trinity.foundation.render.qt.impl.eventconverters;

import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.event.DisplayEventTarget;
import org.trinity.foundation.api.display.event.KeyNotifyEvent;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.render.binding.BindingDiscovery;
import org.trinity.foundation.api.render.binding.BoundKeyInputEvent;
import org.trinity.foundation.render.qt.impl.QJRenderEventConversion;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QKeyEvent;

public abstract class AbstractQJKeyConversion implements QJRenderEventConversion {

	private final BindingDiscovery bindingDiscovery;

	AbstractQJKeyConversion(final BindingDiscovery bindingDiscovery) {
		this.bindingDiscovery = bindingDiscovery;
	}

	@Override
	public KeyNotifyEvent convertEvent(	final DisplayEventTarget source,
										final Object view,
										final QObject qObject,
										final QEvent qEvent) {

		qEvent.accept();
		final QKeyEvent keyEvent = (QKeyEvent) qEvent;

		if (keyEvent.isAutoRepeat()) {
			return null;
		}

		final int keyCode = keyEvent.nativeScanCode();

		final int state = keyEvent.nativeModifiers();

		final Key key = new Key(keyCode);
		final InputModifiers inputModifiers = new InputModifiers(state);
		final KeyboardInput input = new KeyboardInput(	Momentum.STARTED,
														key,
														inputModifiers);

		Optional<String> inputSlotName;
		try {
			inputSlotName = this.bindingDiscovery.lookupInputEmitterInputSlotName(	view,
																					qObject,
																					KeyboardInput.class);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
			inputSlotName = Optional.absent();
		}

		KeyNotifyEvent keyNotifyEvent;
		if (inputSlotName.isPresent()) {
			keyNotifyEvent = new BoundKeyInputEvent(source,
													input,
													inputSlotName.get());
		} else {
			keyNotifyEvent = new KeyNotifyEvent(source,
												input);
		}

		return keyNotifyEvent;
	}

	public abstract Momentum getMomemtum();
}