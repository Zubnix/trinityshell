package org.trinity.shell.geo.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.trinity.foundation.shared.geometry.impl.MarginsImpl;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.impl.manager.GeoManagerLine;
import org.trinity.shell.geo.impl.manager.LayoutPropertyLineImpl;

//TODO test corner cases with margins (eg widths that are less than margins, zero width sizes etc.)
public class GeoManagerLineTest {

	@Mock
	private GeoTransformableRectangle child0;
	@Mock
	private GeoTransformableRectangle child1;
	@Mock
	private GeoTransformableRectangle child2;
	@Mock
	private GeoTransformableRectangle container;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void horizontalResizeParentTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(30);
		Mockito.when(child0Trans.getHeight1()).thenReturn(90);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, true, false);
		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		final GeoTransformation containerTransformation = Mockito
				.mock(GeoTransformation.class);

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.handleContainerChanged(this.container,
				containerTransformation);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(30);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(100);

		Mockito.verify(this.child1, Mockito.times(1)).setX(40);
		Mockito.verify(this.child1, Mockito.times(1)).setY(10);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(5);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(80);

		Mockito.verify(this.child2, Mockito.times(1)).setX(60);
		Mockito.verify(this.child2, Mockito.times(1)).setY(5);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(65);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(90);
	}

	@Test
	public void horizontalResizeChildTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(50);
		Mockito.when(child0Trans.getHeight1()).thenReturn(120);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoTransformation child1Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child1Trans.getWidth1()).thenReturn(120);
		Mockito.when(child1Trans.getHeight1()).thenReturn(60);
		Mockito.when(this.child1.toGeoTransformation()).thenReturn(child1Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, true, false);
		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.onChildResizeRequest(this.child0, child1Trans);
		geoManagerLine.onChildResizeRequest(this.child1, child1Trans);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(50);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(100);

		Mockito.verify(this.child1, Mockito.times(1)).setX(60);
		Mockito.verify(this.child1, Mockito.times(1)).setY(10);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(0);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(80);

		Mockito.verify(this.child2, Mockito.times(1)).setX(75);
		Mockito.verify(this.child2, Mockito.times(1)).setY(5);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(50);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(90);
	}

	@Test
	public void horizontalInverseResizeParentTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(30);
		Mockito.when(child0Trans.getHeight1()).thenReturn(90);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, true, true);
		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		final GeoTransformation containerTransformation = Mockito
				.mock(GeoTransformation.class);

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.handleContainerChanged(this.container,
				containerTransformation);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setX(100);
		Mockito.verify(this.child0, Mockito.times(1)).setY(0);
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(30);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(100);

		Mockito.verify(this.child1, Mockito.times(1)).setX(85);
		Mockito.verify(this.child1, Mockito.times(1)).setY(10);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(5);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(80);

		Mockito.verify(this.child2, Mockito.times(1)).setX(5);
		Mockito.verify(this.child2, Mockito.times(1)).setY(5);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(65);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(90);
	}

	@Test
	public void horizontalInverseResizeChildTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(50);
		Mockito.when(child0Trans.getHeight1()).thenReturn(120);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoTransformation child1Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child1Trans.getWidth1()).thenReturn(120);
		Mockito.when(child1Trans.getHeight1()).thenReturn(60);
		Mockito.when(this.child1.toGeoTransformation()).thenReturn(child1Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, true, true);
		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.onChildResizeRequest(this.child0, child1Trans);
		geoManagerLine.onChildResizeRequest(this.child1, child1Trans);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setX(80);
		Mockito.verify(this.child0, Mockito.times(1)).setY(0);
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(50);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(100);

		Mockito.verify(this.child1, Mockito.times(1)).setX(70);
		Mockito.verify(this.child1, Mockito.times(1)).setY(10);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(0);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(80);

		Mockito.verify(this.child2, Mockito.times(1)).setX(5);
		Mockito.verify(this.child2, Mockito.times(1)).setY(5);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(50);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(90);
	}

	@Test
	public void verticalResizeParentTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(30);
		Mockito.when(child0Trans.getHeight1()).thenReturn(20);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, false, false);
		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		final GeoTransformation containerTransformation = Mockito
				.mock(GeoTransformation.class);

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.handleContainerChanged(this.container,
				containerTransformation);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setX(0);
		Mockito.verify(this.child0, Mockito.times(1)).setY(0);
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(130);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(20);

		Mockito.verify(this.child1, Mockito.times(1)).setX(10);
		Mockito.verify(this.child1, Mockito.times(1)).setY(30);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(110);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(0);

		Mockito.verify(this.child2, Mockito.times(1)).setX(5);
		Mockito.verify(this.child2, Mockito.times(1)).setY(45);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(120);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(50);
	}

	@Test
	public void verticalResizeChildTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(50);
		Mockito.when(child0Trans.getHeight1()).thenReturn(10);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoTransformation child1Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child1Trans.getWidth1()).thenReturn(120);
		Mockito.when(child1Trans.getHeight1()).thenReturn(60);
		Mockito.when(this.child1.toGeoTransformation()).thenReturn(child1Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, false, false);

		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.onChildResizeRequest(this.child0, child1Trans);
		geoManagerLine.onChildResizeRequest(this.child1, child1Trans);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setX(0);
		Mockito.verify(this.child0, Mockito.times(1)).setY(0);
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(130);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(10);

		Mockito.verify(this.child1, Mockito.times(1)).setX(10);
		Mockito.verify(this.child1, Mockito.times(1)).setY(20);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(110);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(3);

		Mockito.verify(this.child2, Mockito.times(1)).setX(5);
		Mockito.verify(this.child2, Mockito.times(1)).setY(38);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(120);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(58);
	}

	@Test
	public void verticalInverseResizeParentTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(30);
		Mockito.when(child0Trans.getHeight1()).thenReturn(20);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, false, true);
		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		final GeoTransformation containerTransformation = Mockito
				.mock(GeoTransformation.class);

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.handleContainerChanged(this.container,
				containerTransformation);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setX(0);
		Mockito.verify(this.child0, Mockito.times(1)).setY(80);
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(130);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(20);

		Mockito.verify(this.child1, Mockito.times(1)).setX(10);
		Mockito.verify(this.child1, Mockito.times(1)).setY(70);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(110);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(0);

		Mockito.verify(this.child2, Mockito.times(1)).setX(5);
		Mockito.verify(this.child2, Mockito.times(1)).setY(5);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(120);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(50);
	}

	@Test
	public void verticalInverseResizeChildTest() {
		// given
		final GeoTransformation child0Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child0Trans.getWidth1()).thenReturn(50);
		Mockito.when(child0Trans.getHeight1()).thenReturn(10);
		Mockito.when(this.child0.toGeoTransformation()).thenReturn(child0Trans);

		final GeoTransformation child1Trans = Mockito
				.mock(GeoTransformation.class);
		Mockito.when(child1Trans.getWidth1()).thenReturn(120);
		Mockito.when(child1Trans.getHeight1()).thenReturn(60);
		Mockito.when(this.child1.toGeoTransformation()).thenReturn(child1Trans);

		final GeoManagerLine geoManagerLine = new GeoManagerLine(
				this.container, false, true);

		geoManagerLine.setContainer(this.container);

		geoManagerLine.addManagedChild(this.child0);
		geoManagerLine.addManagedChild(this.child1, new LayoutPropertyLineImpl(10,
				new MarginsImpl(10, 10)));
		geoManagerLine.addManagedChild(this.child2, new LayoutPropertyLineImpl(30,
				new MarginsImpl(5)));

		Mockito.when(this.container.getWidth()).thenReturn(130);
		Mockito.when(this.container.getHeight()).thenReturn(100);

		// when
		geoManagerLine.onChildResizeRequest(this.child0, child1Trans);
		geoManagerLine.onChildResizeRequest(this.child1, child1Trans);

		// then
		Mockito.verify(this.child0, Mockito.times(1)).setX(0);
		Mockito.verify(this.child0, Mockito.times(1)).setY(90);
		Mockito.verify(this.child0, Mockito.times(1)).setWidth(130);
		Mockito.verify(this.child0, Mockito.times(1)).setHeight(10);

		Mockito.verify(this.child1, Mockito.times(1)).setX(10);
		Mockito.verify(this.child1, Mockito.times(1)).setY(77);
		Mockito.verify(this.child1, Mockito.times(1)).setWidth(110);
		Mockito.verify(this.child1, Mockito.times(1)).setHeight(3);

		Mockito.verify(this.child2, Mockito.times(1)).setX(5);
		Mockito.verify(this.child2, Mockito.times(1)).setY(4);
		Mockito.verify(this.child2, Mockito.times(1)).setWidth(120);
		Mockito.verify(this.child2, Mockito.times(1)).setHeight(58);
	}
}
