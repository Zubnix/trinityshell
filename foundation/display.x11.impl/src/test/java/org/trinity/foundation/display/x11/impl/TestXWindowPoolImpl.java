package org.trinity.foundation.display.x11.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.DisplaySurfaceReferencer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestXWindowPoolImpl {

    @Mock
    private XEventPump xEventPump;
    @Mock
    private DisplaySurfaceFactory displaySurfaceFactory;
    @Mock
    private XWindow xWindow;
    @Mock
    private DisplaySurfaceHandle displaySurfaceHandle;
    @InjectMocks
    private XWindowPoolImpl xWindowPool;

    @Before
    public void setup() {
        final int nativeHandle = 123;
		when(this.displaySurfaceHandle.getNativeHandle()).thenReturn(nativeHandle);
		when(this.displaySurfaceFactory.createDisplaySurface(this.displaySurfaceHandle)).thenReturn(this.xWindow);
		when(this.xWindow.getDisplaySurfaceHandle()).thenReturn(this.displaySurfaceHandle);
	}

    @Test
    public void testLazyPooling() {
        //given
        //a pool

        //when
        //a window is requested more than once
		this.xWindowPool.getDisplaySurface(this.displaySurfaceHandle);
		this.xWindowPool.getDisplaySurface(this.displaySurfaceHandle);

		//then
        //it is lazily created and cached if not present
		verify(this.displaySurfaceFactory,
			   times(1)).createDisplaySurface(this.displaySurfaceHandle);
	}

    @Test
    public void testPoolPresence() {
        //given
        //a pool

        //when
        //a windows pool presence is requested
		final boolean present0 = this.xWindowPool.isPresent(this.displaySurfaceHandle);
		this.xWindowPool.getDisplaySurface(this.displaySurfaceHandle);
		final boolean present1 = this.xWindowPool.isPresent(this.displaySurfaceHandle);

		//then
        //the pool reports if the window is cached
        assertFalse(present0);
        assertTrue(present1);
    }

    @Test
    public void testSynchronizedReferencing() {
        //given
        //a pool

        //when
        //a new serverside window is requested
		try(DisplaySurfaceReferencer displaySurfaceReferencer = this.xWindowPool.getDisplaySurfaceCreator()) {
			displaySurfaceReferencer.reference(this.displaySurfaceHandle);
		}

        //then
        //event processing is halted until the window is cached
		final InOrder inOrder = inOrder(this.xEventPump,
										this.displaySurfaceFactory);

		inOrder.verify(this.xEventPump).stop();
		inOrder.verify(this.displaySurfaceFactory).createDisplaySurface(this.displaySurfaceHandle);
		inOrder.verify(this.xEventPump).start();
	}
}
