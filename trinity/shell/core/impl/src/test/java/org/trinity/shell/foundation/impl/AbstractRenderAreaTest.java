package org.trinity.shell.foundation.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.trinity.shell.MockedEnv;
import org.trinity.shell.foundation.impl.AbstractRenderArea;
import org.trinity.shell.foundation.impl.RenderAreaGeoExecutorImpl;
import org.trinity.shell.geo.api.GeoExecutor;

public class AbstractRenderAreaTest {

	public MockedEnv mockedEnv;

	@Mock
	public RenderAreaGeoExecutorImpl renderAreaGeoExecutor;

	public AbstractRenderArea abstractRenderArea;

	@Before
	public void setUp() {
		this.mockedEnv = new MockedEnv();
		this.mockedEnv.setUp();
		MockitoAnnotations.initMocks(this);
		this.abstractRenderArea = new AbstractRenderArea() {
			@Override
			public GeoExecutor getGeoExecutor() {
				return AbstractRenderAreaTest.this.renderAreaGeoExecutor;
			}
		};
	}

	@Test
	public void testGeometryConstraints() {
		this.abstractRenderArea.setHeight(Integer.MIN_VALUE);
		this.abstractRenderArea.setWidth(Integer.MIN_VALUE);
		this.abstractRenderArea.requestResize();

		Assert.assertEquals(AbstractRenderArea.DEFAULT_MIN_WIDTH,
				this.abstractRenderArea.getWidth());
		Assert.assertEquals(AbstractRenderArea.DEFAULT_MIN_HEIGHT,
				this.abstractRenderArea.getHeight());

		this.abstractRenderArea.setWidth(Integer.MAX_VALUE);
		this.abstractRenderArea.setHeight(Integer.MAX_VALUE);
		this.abstractRenderArea.doResize();

		Assert.assertEquals(AbstractRenderArea.DEFAULT_MAX_WIDTH,
				this.abstractRenderArea.getWidth());
		Assert.assertEquals(AbstractRenderArea.DEFAULT_MAX_HEIGHT,
				this.abstractRenderArea.getHeight());

		this.abstractRenderArea.setWidth(10);
		this.abstractRenderArea.setHeight(10);
		this.abstractRenderArea.doResize();

		this.abstractRenderArea.setWidthIncrement(5);
		this.abstractRenderArea.setHeightIncrement(3);
		this.abstractRenderArea.setWidth(17);
		this.abstractRenderArea.setHeight(12);
		this.abstractRenderArea.doResize();

		Assert.assertEquals(15, this.abstractRenderArea.getWidth());
		Assert.assertEquals(10, this.abstractRenderArea.getHeight());

		this.abstractRenderArea.setMaxWidth(20);
		this.abstractRenderArea.setMaxHeight(20);
		this.abstractRenderArea.setWidth(30);
		this.abstractRenderArea.setHeight(30);
		this.abstractRenderArea.setWidthIncrement(1);
		this.abstractRenderArea.setHeightIncrement(1);
		this.abstractRenderArea.doResize();

		Assert.assertEquals(20, this.abstractRenderArea.getWidth());
		Assert.assertEquals(20, this.abstractRenderArea.getHeight());

		this.abstractRenderArea.setMinWidth(10);
		this.abstractRenderArea.setMinHeight(10);
		this.abstractRenderArea.setWidth(5);
		this.abstractRenderArea.setHeight(5);
		this.abstractRenderArea.doResize();

		Assert.assertEquals(10, this.abstractRenderArea.getWidth());
		Assert.assertEquals(10, this.abstractRenderArea.getHeight());

		this.abstractRenderArea.setX(10);
		this.abstractRenderArea.setY(10);
		this.abstractRenderArea.requestMove();
		this.abstractRenderArea.setMovable(false);
		this.abstractRenderArea.setX(20);
		this.abstractRenderArea.setY(20);
		this.abstractRenderArea.requestMove();

		Assert.assertEquals(10, this.abstractRenderArea.getX());
		Assert.assertEquals(10, this.abstractRenderArea.getY());

		this.abstractRenderArea.setMaxWidth(50);
		this.abstractRenderArea.setMaxHeight(50);
		this.abstractRenderArea.setWidth(25);
		this.abstractRenderArea.setHeight(25);
		this.abstractRenderArea.requestResize();
		this.abstractRenderArea.setResizable(false);
		this.abstractRenderArea.setWidth(20);
		this.abstractRenderArea.setHeight(20);
		this.abstractRenderArea.requestResize();

		Assert.assertEquals(25, this.abstractRenderArea.getWidth());
		Assert.assertEquals(25, this.abstractRenderArea.getHeight());
	}

	@Test
	public void testSyncGeoToPlatformRenderArea() {
		this.abstractRenderArea
				.setManagedDisplay(this.mockedEnv.managedDisplayMock);
		this.abstractRenderArea
				.setPlatformRenderArea(this.mockedEnv.widgetPlatformRenderAreaMock);

		this.abstractRenderArea.syncGeoToPlatformRenderAreaGeo();

		Assert.assertEquals(this.mockedEnv.widgetPlatformRenderAreaX,
				this.abstractRenderArea.getX());
		Assert.assertEquals(this.mockedEnv.widgetPlatformRenderAreaY,
				this.abstractRenderArea.getY());
		Assert.assertEquals(this.mockedEnv.widgetPlatformRenderAreaWidth,
				this.abstractRenderArea.getWidth());
		Assert.assertEquals(this.mockedEnv.widgetPlatformRenderAreaHeight,
				this.abstractRenderArea.getHeight());
	}

	// TODO test property changed handler?
}
