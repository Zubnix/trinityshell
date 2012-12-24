package org.trinity.shellplugin.widget.api.binding;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.PointerInput;

import com.google.common.base.Optional;

public class ViewInputBindingTest {

	@Test
	public void testOnClick() throws ExecutionException {

		DummyView dummyView = new DummyView();

		BindingDiscovery bindingDiscovery = new BindingDiscovery(null);
		Optional<String> slotName = bindingDiscovery.lookupInputEmitterInputSlotName(	dummyView,
																						dummyView,
																						PointerInput.class);
		assertEquals(	"onClick",
						slotName.get());
	}

	@Test
	public void testOnKey() throws ExecutionException {
		DummyView dummyView = new DummyView();

		BindingDiscovery bindingDiscovery = new BindingDiscovery(null);

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

		Object newKeyEventEmitter = new Object();
		dummyView.setKeyEventEmitter(newKeyEventEmitter);
		keySlotName = bindingDiscovery.lookupInputEmitterInputSlotName(	dummyView,
																		dummyView.getKeyEventEmitter(),
																		KeyboardInput.class);
		assertEquals(	"onKey",
						keySlotName.get());
	}
}
