package org.trinity.foundation.api.render.binding;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.PointerInput;

import com.google.common.base.Optional;

public class ViewInputBindingTest {

	@Test
	public void testOnClick() throws ExecutionException {

		final DummyView dummyView = new DummyView();

		final BindingDiscovery bindingDiscovery = new BindingDiscovery(	null,
																		null);
		final Optional<String> slotName = bindingDiscovery.lookupInputEmitterInputSlotName(	dummyView,
																							dummyView,
																							PointerInput.class);
		assertEquals(	"onClick",
						slotName.get());
	}

	@Test
	public void testOnKey() throws ExecutionException {
		final DummyView dummyView = new DummyView();

		final BindingDiscovery bindingDiscovery = new BindingDiscovery(	null,
																		null);

		Optional<String> keySlotName = bindingDiscovery.lookupInputEmitterInputSlotName(dummyView,
																						dummyView,
																						KeyboardInput.class);
		assertEquals(	"onViewKey",
						keySlotName.get());

		keySlotName = bindingDiscovery.lookupInputEmitterInputSlotName(	dummyView,
																		dummyView.getKeyEventEmitter(),
																		KeyboardInput.class);
		assertEquals(	"onKey",
						keySlotName.get());

		final Object newKeyEventEmitter = new Object();
		dummyView.setKeyEventEmitter(newKeyEventEmitter);
		keySlotName = bindingDiscovery.lookupInputEmitterInputSlotName(	dummyView,
																		dummyView.getKeyEventEmitter(),
																		KeyboardInput.class);
		assertEquals(	"onKey",
						keySlotName.get());
	}
}
