package org.hyperdrive.unit.geo;

import org.hyperdrive.geo.GeoManagerDirect;
import org.hyperdrive.geo.GeoTransformation;
import org.mockito.Mockito;

/**
 * This class tests all possible geometric operations that can occur on a
 * rectangle that has a parent with a direct geomanager.
 */
public class TestGeoManagerDirect extends TestTemplateGeoManager {

	// TODO implement recurring objects & events into easy to use methods

	@Override
	public void testSizePlaceChild() {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		parent.setGeoManager(geoManagerDirect);
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onMoveResizeRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.only()).doUpdateSizePlaceValue();
	}

	@Override
	public void testSizeChild() {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		parent.setGeoManager(geoManagerDirect);
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onResizeRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.only()).doUpdateSizeValue();
	}

	@Override
	public void testPlaceChild() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		parent.setGeoManager(geoManagerDirect);
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onMoveRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.only()).doUpdatePlaceValue();
	}

	@Override
	public void testVisibilityChild() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		parent.setGeoManager(geoManagerDirect);
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onChangeVisibilityRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.only()).doUpdateVisibility();
	}

	@Override
	public void testReparentingChild() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		parent.setGeoManager(geoManagerDirect);
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onChangeParentRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.only()).doUpdateParentValue();
	}

	@Override
	public void testSizePlaceParent() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub grandParent = new GeoVirtRectangleStub();
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, grandParent, 20, 20, 200, 200, false, grandParent);

		final GeoVirtRectangleStub parent = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onMoveResizeRequest(parent, trans);

		// **THEN**
		Mockito.verify(parent, Mockito.only()).doUpdateSizePlaceValue();
	}

	@Override
	public void testSizeParent() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub grandParent = new GeoVirtRectangleStub();
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, grandParent, 20, 20, 200, 200, false, grandParent);

		final GeoVirtRectangleStub parent = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onResizeRequest(parent, trans);

		// **THEN**
		Mockito.verify(parent, Mockito.only()).doUpdateSizeValue();
	}

	@Override
	public void testPlaceParent() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub grandParent = new GeoVirtRectangleStub();
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, grandParent, 20, 20, 200, 200, false, grandParent);

		final GeoVirtRectangleStub parent = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onMoveRequest(parent, trans);

		// **THEN**
		Mockito.verify(parent, Mockito.only()).doUpdatePlaceValue();
	}

	@Override
	public void testVisibilityParent() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub grandParent = new GeoVirtRectangleStub();
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, grandParent, 20, 20, 200, 200, false, grandParent);

		final GeoVirtRectangleStub parent = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onChangeVisibilityRequest(parent, trans);

		// **THEN**
		Mockito.verify(parent, Mockito.only()).doUpdateVisibility();
	}

	@Override
	public void testReparentingParent() throws Exception {
		// **GIVEN**
		final GeoManagerDirect geoManagerDirect = new GeoManagerDirect();
		final GeoVirtRectangleStub grandParent = new GeoVirtRectangleStub();
		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, grandParent, 20, 20, 200, 200, false, grandParent);

		final GeoVirtRectangleStub parent = Mockito
				.mock(GeoVirtRectangleStub.class);

		// ** WHEN **
		geoManagerDirect.onChangeParentRequest(parent, trans);

		// **THEN**
		Mockito.verify(parent, Mockito.only()).doUpdateParentValue();

	}
}
