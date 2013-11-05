package org.trinity.foundation.display.x11.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.foundation.api.display.DisplaySurfaceCreator;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;

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
        when(displaySurfaceHandle.getNativeHandle()).thenReturn(nativeHandle);
        when(displaySurfaceFactory.createDisplaySurface(displaySurfaceHandle)).thenReturn(xWindow);
        when(xWindow.getDisplaySurfaceHandle()).thenReturn(displaySurfaceHandle);
    }

    @Test
    public void testLazyPooling() {
        //given
        //a pool

        //when
        //a window is requested more than once
        xWindowPool.getDisplaySurface(displaySurfaceHandle);
        xWindowPool.getDisplaySurface(displaySurfaceHandle);

        //then
        //it is lazily created and cached if not present
        verify(displaySurfaceFactory,
               times(1)).createDisplaySurface(displaySurfaceHandle);
    }

    @Test
    public void testPoolPresence() {
        //given
        //a pool

        //when
        //a windows pool presence is requested
        final boolean present0 = xWindowPool.isPresent(displaySurfaceHandle);
        xWindowPool.getDisplaySurface(displaySurfaceHandle);
        final boolean present1 = xWindowPool.isPresent(displaySurfaceHandle);

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
        try(DisplaySurfaceCreator displaySurfaceCreator = xWindowPool.getDisplaySurfaceCreator()){
            displaySurfaceCreator.reference(displaySurfaceHandle);
        }

        //then
        //event processing is halted until the window is cached
        final InOrder inOrder = inOrder(xEventPump,displaySurfaceFactory);

        inOrder.verify(xEventPump).stop();
        inOrder.verify(displaySurfaceFactory).createDisplaySurface(displaySurfaceHandle);
        inOrder.verify(xEventPump).start();
    }
}
