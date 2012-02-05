package org.hyperdrive.unit.geo;

import org.hyperdrive.geo.GeoManager;
import org.junit.Test;

/**
 * This class tests all possible geometric operations that can occur on a
 * rectangle that has a parent with a geomanager. Classes extending
 * {@link GeoManager} should extend this test class and implement each of the
 * required test cases.
 */
public abstract class TestTemplateGeoManager {

	@Test
	public abstract void testSizePlaceChild() throws Exception;

	@Test
	public abstract void testSizeChild() throws Exception;

	@Test
	public abstract void testPlaceChild() throws Exception;

	@Test
	public abstract void testVisibilityChild() throws Exception;

	@Test
	public abstract void testReparentingChild() throws Exception;

	@Test
	public abstract void testSizePlaceParent() throws Exception;

	@Test
	public abstract void testSizeParent() throws Exception;

	@Test
	public abstract void testPlaceParent() throws Exception;

	@Test
	public abstract void testVisibilityParent() throws Exception;

	@Test
	public abstract void testReparentingParent() throws Exception;
}
