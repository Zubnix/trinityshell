package org.trinity.foundation.display.x11.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestXWindowPoolImpl {

	@Mock
	private XEventChannel          xEventPump;
	@Mock
	private XWindowFactory  displaySurfaceFactory;
	@Mock
	private XWindow                xWindow;
	@Mock
	private DisplaySurfaceHandle   displaySurfaceHandle;
	@InjectMocks
	private DisplaySurfacePoolImpl xWindowPool;

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

    @Test
    public void testPoolPresence() {
        //given
        //a pool

        //when
        //a displaySurfaces pool presence is requested
		final boolean present0 = this.xWindowPool.isPresent(this.displaySurfaceHandle);
		this.xWindowPool.get(this.displaySurfaceHandle);
		final boolean present1 = this.xWindowPool.isPresent(this.displaySurfaceHandle);

		//then
        //the pool reports if the window is cached
        assertFalse(present0);
        assertTrue(present1);
    }
}
