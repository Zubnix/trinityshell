package org.trinity.foundation.display.x11.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class XWindowHandleTest {

    @Test
    public void testEquals() {
        //given
        //2 XWindowHandles with the same native handle
        final XWindowHandle xWindowHandle0 = new XWindowHandle(1234);
        final XWindowHandle xWindowHandle1 = new XWindowHandle(1234);

        //when
        //the 2 XWindowHandles are compared for equality
        final boolean equals0 = xWindowHandle0.equals(xWindowHandle1);
        final boolean equals1 = xWindowHandle1.equals(xWindowHandle0);

        //then
        //both XWindowHandles are considered equal
        assertTrue(equals0);
        assertTrue(equals1);
    }

    @Test
    public void testHashCode() {
        //given
        //2 XWindowHandles with the same native handle
        final XWindowHandle xWindowHandle0 = new XWindowHandle(1234);
        final XWindowHandle xWindowHandle1 = new XWindowHandle(1234);

        //when
        //the 2 XWindowHandles hashcodes are compared
        final int hashCode0 = xWindowHandle0.hashCode();
        final int hashCode1 = xWindowHandle1.hashCode();

        //then
        //both XWindowHandles hashcodes are the same
        assertEquals(hashCode0, hashCode1);
    }
}
