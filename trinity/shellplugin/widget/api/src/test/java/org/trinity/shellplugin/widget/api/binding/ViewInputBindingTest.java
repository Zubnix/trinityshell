package org.trinity.shellplugin.widget.api.binding;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
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

	}

	@Test
	public void testOnKey() {

	}

	@Test
	public void testAnyKeyInput() {

	}

	@Test
	public void testOnAny() {

	}
}
