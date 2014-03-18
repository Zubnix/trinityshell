package org.trinity.foundation.display.x11.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.x11.defaul.XCompositor;
import org.trinity.x11.defaul.XSurfacePool;

@RunWith(MockitoJUnitRunner.class)
public class TestXWindowPoolImpl {

	@Mock
	private XCompositor xCompositor;
	@InjectMocks
	private XSurfacePool xSurfacePool;

	@Before
	public void setup() {
	}

	@Test
	public void testLazyPooling() {
	}
}
