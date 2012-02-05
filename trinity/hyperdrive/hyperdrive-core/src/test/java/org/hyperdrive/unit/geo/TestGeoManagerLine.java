/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.unit.geo;

import org.hyperdrive.geo.GeoManagerLine;
import org.hyperdrive.geo.GeoTransformation;
import org.mockito.Mockito;

// TODO documentation
//TODO evaluate layout algoritm corner cases (negative values that shouldn't
//be negative. childs with size 0, ...)
//TODO refactor to reuse code and for cleaner reading
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class TestGeoManagerLine extends TestTemplateGeoManagerWithChildren {

	@Override
	public void testSizePlaceChild() throws Exception {
		// **GIVEN**
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(parent, true,
				false);
		parent.setGeoManager(geoManagerLine);
		geoManagerLine.addManagedChild(child);

		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		// **WHEN**
		geoManagerLine.onMoveResizeRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.atLeastOnce()).cancelPendingMove();
		Mockito.verify(child, Mockito.atMost(1)).cancelPendingMove();

		Mockito.verify(child, Mockito.atLeastOnce()).cancelPendingResize();
		Mockito.verify(child, Mockito.atMost(1)).cancelPendingResize();

		Mockito.verifyNoMoreInteractions(child);
	}

	@Override
	public void testSizeChild() throws Exception {
		// **GIVEN**
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(parent, true,
				false);
		parent.setGeoManager(geoManagerLine);
		geoManagerLine.addManagedChild(child);

		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		// **WHEN**
		geoManagerLine.onResizeRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.atLeastOnce()).cancelPendingMove();
		Mockito.verify(child, Mockito.atMost(1)).cancelPendingMove();

		Mockito.verify(child, Mockito.atLeastOnce()).cancelPendingResize();
		Mockito.verify(child, Mockito.atMost(1)).cancelPendingResize();

		Mockito.verifyNoMoreInteractions(child);
	}

	@Override
	public void testPlaceChild() throws Exception {
		// **GIVEN**
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(parent, true,
				false);
		parent.setGeoManager(geoManagerLine);
		geoManagerLine.addManagedChild(child);

		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, false, parent);

		// **WHEN**
		geoManagerLine.onMoveRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.atLeastOnce()).cancelPendingMove();
		Mockito.verify(child, Mockito.atMost(1)).cancelPendingMove();

		Mockito.verify(child, Mockito.atLeastOnce()).cancelPendingResize();
		Mockito.verify(child, Mockito.atMost(1)).cancelPendingResize();

		Mockito.verifyNoMoreInteractions(child);
	}

	@Override
	public void testVisibilityChild() throws Exception {
		// **GIVEN**
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(parent, true,
				false);
		parent.setGeoManager(geoManagerLine);
		geoManagerLine.addManagedChild(child);

		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, true, parent);

		// **WHEN**
		geoManagerLine.onChangeVisibilityRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.atLeastOnce()).doUpdateVisibility();
		Mockito.verify(child, Mockito.atMost(1)).doUpdateVisibility();

		Mockito.verifyNoMoreInteractions(child);
	}

	@Override
	public void testReparentingChild() throws Exception {
		// **GIVEN**
		final GeoVirtRectangleStub parent = new GeoVirtRectangleStub();
		final GeoVirtRectangleStub newParent = new GeoVirtRectangleStub();
		final GeoVirtRectangleStub child = Mockito
				.mock(GeoVirtRectangleStub.class);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(parent, true,
				false);
		parent.setGeoManager(geoManagerLine);
		geoManagerLine.addManagedChild(child);

		final GeoTransformation trans = new GeoTransformation(0, 0, 0, 0,
				false, parent, 20, 20, 200, 200, true, newParent);

		// **WHEN**
		geoManagerLine.onChangeParentRequest(child, trans);

		// **THEN**
		Mockito.verify(child, Mockito.atLeastOnce()).doUpdateParentValue();
		Mockito.verify(child, Mockito.atMost(1)).doUpdateParentValue();
	}

	@Override
	public void testSizePlacePeerChild() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testSizePeerChild() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testPlacePeerChild() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testVisibilityPeerChild() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testReparentingPeerChild() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testSizePlaceParent() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testSizeParent() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testPlaceParent() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testVisibilityParent() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testReparentingParent() throws Exception {
		// TODO Auto-generated method stub

	}
}
