package org.trinity.shell.geo.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.impl.GeoTransformationImpl;
import org.trinity.shell.geo.impl.GeoVirtGeoExecutor;
import org.trinity.shell.geo.impl.GeoVirtRectangle;

public class GeoVirtRectGeoExecutorTest {

	@Mock
	private GeoVirtGeoExecutor geoExecutor;

	@Mock
	private GeoTransformableRectangle parent;

	@Mock
	private GeoVirtRectangle virtRectangle;

	@Mock
	private GeoTransformableRectangle child;

	@Mock
	private GeoExecutor childGeoExecutor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		// always given
		Mockito.when(this.child.getGeoExecutor()).thenReturn(
				this.childGeoExecutor);
		Mockito.when(this.virtRectangle.getChildren()).thenReturn(
				new GeoTransformableRectangle[] { this.child });
		this.geoExecutor = new GeoVirtGeoExecutor(this.virtRectangle);
	}

	@Test
	public void testDestroy() {

	}

	@Test
	public void testUpdateParent() {

	}

	@Test
	public void testUpdatePlace() {
		// given
		final GeoTransformationImpl geoTransformation = new GeoTransformationImpl(
				5, 10, 30, 40, true, this.parent, 12, 28, 30, 40, true,
				this.parent);
		Mockito.when(this.virtRectangle.toGeoTransformation()).thenReturn(
				geoTransformation);
		Mockito.when(this.child.getX()).thenReturn(3);
		Mockito.when(this.child.getY()).thenReturn(11);

		// when
		this.geoExecutor.updatePlace(12, 28);

		// then
		Mockito.verify(this.childGeoExecutor, Mockito.times(1)).updatePlace(
				3 + (12 - 5), 11 + (28 - 10));
	}

	@Test
	public void testUpdateSize() {
		// when
		this.geoExecutor.updateSize(123, 123);
		// then
		Mockito.verifyZeroInteractions(this.childGeoExecutor);
	}

	@Test
	public void testUpdateSizePlace() {
		// given
		final GeoTransformationImpl geoTransformation = new GeoTransformationImpl(
				5, 10, 30, 40, true, this.parent, 12, 28, 30, 40, true,
				this.parent);
		Mockito.when(this.virtRectangle.toGeoTransformation()).thenReturn(
				geoTransformation);
		Mockito.when(this.child.getX()).thenReturn(3);
		Mockito.when(this.child.getY()).thenReturn(11);

		// when
		this.geoExecutor.updatePlace(12, 28);

		// then
		Mockito.verify(this.childGeoExecutor, Mockito.times(1)).updatePlace(
				3 + (12 - 5), 11 + (28 - 10));
	}

	@Test
	public void testUpdateVisibility() {
		// when
		this.geoExecutor.updateVisibility(false);
		// then
		Mockito.verify(this.childGeoExecutor, Mockito.times(1))
				.updateVisibility(this.child.isVisible() && false);
	}

	@Test
	public void testRaise() {
		// when
		this.geoExecutor.raise();
		// then
		Mockito.verify(this.childGeoExecutor, Mockito.times(1)).raise();
	}

	@Test
	public void testLower() {
		// when
		this.geoExecutor.lower();
		// then
		Mockito.verify(this.childGeoExecutor, Mockito.times(1)).lower();
	}
}
