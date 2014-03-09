package org.trinity.foundation.display.x11.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestXWindowPoolImpl {

	@Mock
	private XEventChannel        xEventPump;
	@Mock
	private XWindowFactory       displaySurfaceFactory;
	@Mock
	private XWindow              xWindow;
	@Mock
	private DisplaySurfaceHandle displaySurfaceHandle;
	@InjectMocks
	private DisplaySurfacePool   xWindowPool;

	@Before
	public void setup() {
		final int nativeHandle = 123;
		when(this.displaySurfaceHandle.getNativeHandle()).thenReturn(nativeHandle);
		when(this.displaySurfaceFactory.create(this.displaySurfaceHandle)).thenReturn(this.xWindow);
		when(this.xWindow.getDisplaySurfaceHandle()).thenReturn(this.displaySurfaceHandle);
	}

	@Test
	public void testLazyPooling() {
		//given
		//a pool

		//when
		//a window is requested more than once
		this.xWindowPool.get(this.displaySurfaceHandle);
		this.xWindowPool.get(this.displaySurfaceHandle);

		//then
		//it is lazily created and cached if not present
		verify(this.displaySurfaceFactory,
			   times(1)).create(this.displaySurfaceHandle);
	}
}
