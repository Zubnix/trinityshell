package org.trinity.x11.defaul;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.x11.defaul.render.XShellCompositor;

@RunWith(MockitoJUnitRunner.class)
public class TestXWindowPoolImpl {

	@Mock
	private XShellCompositor xCompositor;
	@InjectMocks
	private XSurfacePool xSurfacePool;

	@Before
	public void setup() {
	}

	@Test
	public void testLazyPooling() {
	}
}
