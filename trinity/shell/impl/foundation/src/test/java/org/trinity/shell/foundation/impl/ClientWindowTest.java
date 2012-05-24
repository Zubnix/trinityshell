package org.trinity.shell.foundation.impl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.trinity.core.display.api.event.ConfigureRequestEvent;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.display.api.event.MapRequestEvent;
import org.trinity.core.display.api.event.UnmappedNotifyEvent;
import org.trinity.shell.MockedEnv;
import org.trinity.shell.foundation.impl.ClientWindow;
import org.trinity.shell.geo.api.GeoTransformation;

public class ClientWindowTest {

	private final MockedEnv mockedEnv = new MockedEnv();

	private ClientWindow clientWindow;

	@Before
	public void setUp() {
		this.mockedEnv.setUp();
		this.clientWindow = new ClientWindow(this.mockedEnv.managedDisplayMock,
				this.mockedEnv.c0);
	}

	@Test
	public void testHandleConfigureEvent() {

		final ConfigureRequestEvent configureRequestEvent = Mockito
				.mock(ConfigureRequestEvent.class);
		Mockito.when(configureRequestEvent.getEventSource()).thenReturn(
				this.mockedEnv.c0);
		Mockito.when(configureRequestEvent.getType()).thenReturn(
				DisplayEventType.CONFIGURE_REQUEST);
		Mockito.when(configureRequestEvent.getX()).thenReturn(5);
		Mockito.when(configureRequestEvent.getY()).thenReturn(10);
		Mockito.when(configureRequestEvent.getWidth()).thenReturn(100);
		Mockito.when(configureRequestEvent.getHeight()).thenReturn(150);
		Mockito.when(configureRequestEvent.isXSet()).thenReturn(true);
		Mockito.when(configureRequestEvent.isYSet()).thenReturn(true);
		Mockito.when(configureRequestEvent.isWidthSet()).thenReturn(true);
		Mockito.when(configureRequestEvent.isHeightSet()).thenReturn(true);

		this.clientWindow.fireEvent(configureRequestEvent);
		final GeoTransformation geoTransformation = this.clientWindow
				.toGeoTransformation();

		Assert.assertEquals(5, geoTransformation.getX1());
		Assert.assertEquals(10, geoTransformation.getY1());
		Assert.assertEquals(100, geoTransformation.getWidth1());
		Assert.assertEquals(150, geoTransformation.getHeight1());
	}

	@Test
	public void testHandleMapEvent() {

		final MapRequestEvent mapRequestEvent = Mockito
				.mock(MapRequestEvent.class);
		Mockito.when(mapRequestEvent.getEventSource()).thenReturn(
				this.mockedEnv.c0);
		Mockito.when(mapRequestEvent.getType()).thenReturn(
				DisplayEventType.MAP_REQUEST);

		this.clientWindow.fireEvent(mapRequestEvent);
		final GeoTransformation geoTransformation = this.clientWindow
				.toGeoTransformation();

		Assert.assertEquals(true, geoTransformation.isVisible1());
	}

	@Test
	public void testHandleUnmapEvent() {

		final UnmappedNotifyEvent unmappedNotifyEvent = Mockito
				.mock(UnmappedNotifyEvent.class);
		Mockito.when(unmappedNotifyEvent.getEventSource()).thenReturn(
				this.mockedEnv.c0);
		Mockito.when(unmappedNotifyEvent.getType()).thenReturn(
				DisplayEventType.UNMAP_NOTIFY);

		this.clientWindow.fireEvent(unmappedNotifyEvent);

		Assert.assertEquals(false, this.clientWindow.isVisible());
	}

	@Test
	public void testSetPlatformRenderArea() {

		this.clientWindow.setPlatformRenderArea(this.mockedEnv.c0);

		Assert.assertEquals(this.mockedEnv.c0X, this.clientWindow.getX());
		Assert.assertEquals(this.mockedEnv.c0Y, this.clientWindow.getY());
		Assert.assertEquals(this.mockedEnv.c0Width, this.clientWindow.getWidth());
		Assert.assertEquals(this.mockedEnv.c0Height,
				this.clientWindow.getHeight());
	}
}
