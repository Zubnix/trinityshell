package org.trinity.shell.geo.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.impl.AbstractGeoTransformableRectangle;

public class AbstractGeoTransRectGeoExecutorDelegationTest {

	@Mock
	public GeoExecutor geoExecutor;

	public GeoTransformableRectangle geoTransformableRectangle;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testFindDominantGeomanager() {
		final GeoTransformableRectangle parentGeoTransformableRectangle = Mockito
				.mock(GeoTransformableRectangle.class);

		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}

			@Override
			public GeoTransformableRectangle getParent() {
				return parentGeoTransformableRectangle;
			}
		};

		// when
		this.geoTransformableRectangle.getParentGeoManager();

		// then
		Mockito.verify(parentGeoTransformableRectangle, Mockito.times(1))
				.getGeoManager();
	}

	@Test
	public void testCancelPendings() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		this.geoTransformableRectangle.setX(12);
		this.geoTransformableRectangle.setY(34);
		this.geoTransformableRectangle.setWidth(56);
		this.geoTransformableRectangle.setHeight(78);

		// when
		this.geoTransformableRectangle.cancelPendingMove();
		this.geoTransformableRectangle.cancelPendingResize();

		// then
		final GeoTransformation geoTransformation = this.geoTransformableRectangle
				.toGeoTransformation();
		Assert.assertEquals(0, geoTransformation.getDeltaX());
		Assert.assertEquals(0, geoTransformation.getDeltaY());
		Assert.assertEquals(0, geoTransformation.getDeltaWidth());
		Assert.assertEquals(0, geoTransformation.getDeltaHeight());
	}

	@Test
	public void testDestroy() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		// when
		this.geoTransformableRectangle.doDestroy();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).destroy();
	}

	@Test
	public void testChangeVisibility() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		// when

		this.geoTransformableRectangle.doUpdateVisibility();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).updateVisibility(
				this.geoTransformableRectangle.toGeoTransformation()
						.isVisible1());
	}

	@Test
	public void testLower() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		// when
		this.geoTransformableRectangle.doLower();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).lower();
	}

	@Test
	public void testRaise() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		// when
		this.geoTransformableRectangle.doRaise();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).raise();
	}

	@Test
	public void testResize() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		// when
		this.geoTransformableRectangle.setWidth(98);
		this.geoTransformableRectangle.setHeight(76);
		this.geoTransformableRectangle.doResize();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).updateSize(98, 76);
	}

	@Test
	public void testMoveResize() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		// when
		this.geoTransformableRectangle.setX(43);
		this.geoTransformableRectangle.setY(21);
		this.geoTransformableRectangle.setWidth(87);
		this.geoTransformableRectangle.setHeight(65);
		this.geoTransformableRectangle.doMoveResize();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).updateSizePlace(43,
				21, 87, 65);
	}

	@Test
	public void testMove() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		// when
		this.geoTransformableRectangle.setX(23);
		this.geoTransformableRectangle.setY(45);
		this.geoTransformableRectangle.doMove();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).updatePlace(23, 45);
	}

	@Test
	public void testReparent() {
		// given
		this.geoTransformableRectangle = new AbstractGeoTransformableRectangle() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractGeoTransRectGeoExecutorDelegationTest.this.geoExecutor;
			}
		};

		final GeoTransformableRectangle parentGeoTransformableRectangle = Mockito
				.mock(GeoTransformableRectangle.class);

		// when
		this.geoTransformableRectangle
				.setParent(parentGeoTransformableRectangle);
		this.geoTransformableRectangle.doReparent();

		// then
		Mockito.verify(this.geoExecutor, Mockito.times(1)).updateParent(
				parentGeoTransformableRectangle);
	}
}
