package org.hyperdrive.geo;

import org.hyperdrive.api.geo.GeoTransformableRectangle;
import org.junit.Test;
import org.mockito.Mockito;

public class GeoManagerLineTest {

	@Test
	public void horizontalResizeParentTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				true, false);

		// when
		// geoManagerLine.handleContainerChanged(container, transformation);

		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void horizontalResizeChildTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				true, false);

		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void horizontalInverseResizeParentTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				true, true);

		// when

		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void horizontalInverseResizeChildTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				true, true);

		// when

		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalResizeParentTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				false, false);

		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalResizeChildTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				false, false);

		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalInverseResizeParentTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				false, true);

		throw new RuntimeException("Not yet implemented");
	}

	@Test
	public void verticalInverseResizeChildTest() {
		// given
		final GeoTransformableRectangle container = Mockito
				.mock(GeoTransformableRectangle.class);
		final GeoManagerLine geoManagerLine = new GeoManagerLine(container,
				false, true);

		throw new RuntimeException("Not yet implemented");
	}
}
