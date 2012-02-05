package org.hyperdrive.unit.geo;

import org.junit.Test;

public abstract class TestTemplateGeoManagerWithChildren extends
		TestTemplateGeoManager {

	@Test
	public abstract void testSizePlacePeerChild() throws Exception;

	@Test
	public abstract void testSizePeerChild() throws Exception;

	@Test
	public abstract void testPlacePeerChild() throws Exception;

	@Test
	public abstract void testVisibilityPeerChild() throws Exception;

	@Test
	public abstract void testReparentingPeerChild() throws Exception;
}
