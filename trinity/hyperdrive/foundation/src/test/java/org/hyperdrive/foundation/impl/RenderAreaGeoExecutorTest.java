package org.hyperdrive.foundation.impl;

import org.hydrogen.display.api.PlatformRenderArea;
import org.hydrogen.geometry.api.Coordinates;
import org.hyperdrive.MockedEnv;
import org.hyperdrive.core.api.RenderArea;
import org.hyperdrive.foundation.api.base.BaseRenderAreaGeoExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

//TODO split this class into a parameterized test. parameters are: platformrenderarea of renderarea, parent and platformrenderea of parent
public class RenderAreaGeoExecutorTest {

	private MockedEnv mockedEnv;

	private BaseRenderAreaGeoExecutor renderAreaGeoExecutor;

	@Mock
	private RenderArea renderArea;

	@Mock
	private PlatformRenderArea platformRenderArea;

	@Before
	public void setUp() {
		this.mockedEnv = new MockedEnv();
		this.mockedEnv.setUp();

		MockitoAnnotations.initMocks(this);

		Mockito.when(this.renderArea.getManagedDisplay()).thenReturn(
				this.mockedEnv.managedDisplayMock);
		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);

		this.renderAreaGeoExecutor = new BaseRenderAreaGeoExecutor(this.renderArea);
	}

	@Test
	public void testDestroy() {
		this.renderAreaGeoExecutor.destroy();
		Mockito.verify(this.platformRenderArea, Mockito.times(1)).destroy();
	}

	@Test
	public void testUpdateRenderAreaParentSamePlatformRenderAreaParentInitializedSelfInitialized() {
		// given
		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);
		Mockito.when(parentRenderArea.getX()).thenReturn(15);
		Mockito.when(parentRenderArea.getY()).thenReturn(20);
		Mockito.when(parentRenderArea.getAbsoluteX()).thenReturn(55);
		Mockito.when(parentRenderArea.getAbsoluteY()).thenReturn(75);

		Mockito.when(parentRenderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);

		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);
		Mockito.when(this.renderArea.getX()).thenReturn(5);
		Mockito.when(this.renderArea.getY()).thenReturn(10);

		// when
		this.renderAreaGeoExecutor.updateParent(parentRenderArea);

		// then
		Mockito.verify(this.platformRenderArea, Mockito.times(1)).setParent(
				this.platformRenderArea, 60, 85);
	}

	@Test
	public void testUpdateRenderAreaParentSamePlatformRenderAreaParentNotInitializedSelfInitialized() {
		// given
		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);

		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);
		Mockito.when(this.renderArea.getX()).thenReturn(5);
		Mockito.when(this.renderArea.getY()).thenReturn(10);

		// when
		this.renderAreaGeoExecutor.updateParent(parentRenderArea);

		// then
		// TODO instead of directly hiding the area, we should make isVisible
		// return false and make the desired visibility return whatever
		// isVisible returned before.
		Mockito.verify(this.platformRenderArea, Mockito.times(1)).hide();
	}

	@Test
	public void testUpdateRenderAreaParentSamePlatformRenderAreaParentNotInitializedSelfNotInitialized() {
		// given
		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);

		Mockito.when(this.renderArea.getX()).thenReturn(5);
		Mockito.when(this.renderArea.getY()).thenReturn(10);

		// when
		this.renderAreaGeoExecutor.updateParent(parentRenderArea);

		// then
		Mockito.verify(this.renderArea, Mockito.times(1))
				.getPlatformRenderArea();
		Mockito.verify(parentRenderArea, Mockito.times(1))
				.getPlatformRenderArea();
		Mockito.verifyNoMoreInteractions(this.renderArea, parentRenderArea);
	}

	@Test
	public void testUpdateRenderAreaParentSamePlatformRenderAreaParentInitializedSelfNotInitialized() {
		// given
		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);
		Mockito.when(parentRenderArea.getX()).thenReturn(15);
		Mockito.when(parentRenderArea.getY()).thenReturn(20);
		Mockito.when(parentRenderArea.getAbsoluteX()).thenReturn(55);
		Mockito.when(parentRenderArea.getAbsoluteY()).thenReturn(75);

		Mockito.when(this.renderArea.getX()).thenReturn(5);
		Mockito.when(this.renderArea.getY()).thenReturn(10);

		// when
		this.renderAreaGeoExecutor.updateParent(parentRenderArea);

		// then
		Mockito.verify(this.renderArea, Mockito.times(1))
				.getPlatformRenderArea();
		Mockito.verify(parentRenderArea, Mockito.times(1))
				.getPlatformRenderArea();
		Mockito.verifyNoMoreInteractions(this.renderArea, parentRenderArea);
	}

	@Test
	public void testUpdateRenderAreaParentDifferentPlatformRenderAreaParentInitializedSelfInitialized() {
		// given
		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);
		Mockito.when(this.renderArea.getX()).thenReturn(5);
		Mockito.when(this.renderArea.getY()).thenReturn(10);

		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);

		final PlatformRenderArea parentPlatformRenderArea = Mockito
				.mock(PlatformRenderArea.class);
		Mockito.when(parentRenderArea.getPlatformRenderArea()).thenReturn(
				parentPlatformRenderArea);
		Mockito.when(parentRenderArea.getX()).thenReturn(15);
		Mockito.when(parentRenderArea.getY()).thenReturn(20);
		Mockito.when(parentRenderArea.getAbsoluteX()).thenReturn(40);
		Mockito.when(parentRenderArea.getAbsoluteY()).thenReturn(35);

		Mockito.when(
				this.mockedEnv.widgetPlatformRenderAreaMock
						.translateCoordinates(parentPlatformRenderArea, 0, 0))
				.thenReturn(new Coordinates() {
					@Override
					public int getX() {
						return 25;
					}

					@Override
					public int getY() {
						return 15;
					}

				});

		// when
		this.renderAreaGeoExecutor.updateParent(parentRenderArea);

		// then
		Mockito.verify(this.platformRenderArea, Mockito.times(1)).setParent(
				parentPlatformRenderArea, 20, 30);
	}

	@Test
	public void testUpdatePlaceWithSamePlatformRenderAreaAndRenderAreaParent() {
		// given
		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);
		Mockito.when(parentRenderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);
		Mockito.when(parentRenderArea.getX()).thenReturn(15);
		Mockito.when(parentRenderArea.getY()).thenReturn(20);
		Mockito.when(parentRenderArea.getAbsoluteX()).thenReturn(65);
		Mockito.when(parentRenderArea.getAbsoluteY()).thenReturn(25);

		Mockito.when(this.renderArea.getParent()).thenReturn(parentRenderArea);
		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);

		// when
		this.renderAreaGeoExecutor.updatePlace(10, 20);

		// then
		Mockito.verify(this.platformRenderArea, Mockito.never()).move(
				Matchers.anyInt(), Matchers.anyInt());
	}

	@Test
	public void testUpdatePlaceWithDifferentParentPlatformRenderAreaAndRenderAreaParent() {
		// given
		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);

		final PlatformRenderArea parentPlatformRenderArea = Mockito
				.mock(PlatformRenderArea.class);
		Mockito.when(parentRenderArea.getPlatformRenderArea()).thenReturn(
				parentPlatformRenderArea);

		Mockito.when(parentRenderArea.getX()).thenReturn(15);
		Mockito.when(parentRenderArea.getY()).thenReturn(20);
		Mockito.when(parentRenderArea.getAbsoluteX()).thenReturn(65);
		Mockito.when(parentRenderArea.getAbsoluteY()).thenReturn(25);

		Mockito.when(this.renderArea.getParent()).thenReturn(parentRenderArea);
		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);

		Mockito.when(
				this.mockedEnv.widgetPlatformRenderAreaMock
						.translateCoordinates(this.platformRenderArea, 0, 0))
				.thenReturn(new Coordinates() {
					@Override
					public int getX() {
						return 25;
					}

					@Override
					public int getY() {
						return 15;
					}

				});
		// when
		this.renderAreaGeoExecutor.updatePlace(10, 20);

		// then
		Mockito.verify(this.platformRenderArea, Mockito.times(1)).move(50, 30);
	}

	@Test
	public void testUpdateSize() {
		// given
		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);
		// when
		this.renderAreaGeoExecutor.updateSize(12, 34);
		// then
		Mockito.verify(this.platformRenderArea, Mockito.times(1))
				.resize(12, 34);
	}

	@Test
	public void testUpdateSizePlace() {
		// given
		final RenderArea parentRenderArea = Mockito.mock(RenderArea.class);

		final PlatformRenderArea parentPlatformRenderArea = Mockito
				.mock(PlatformRenderArea.class);
		Mockito.when(parentRenderArea.getPlatformRenderArea()).thenReturn(
				parentPlatformRenderArea);
		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);
		Mockito.when(this.renderArea.getParent()).thenReturn(parentRenderArea);

		// when
		this.renderAreaGeoExecutor.updateSizePlace(1, 2, 34, 56);
		// then
		Mockito.verify(this.platformRenderArea, Mockito.times(1)).moveResize(1,
				2, 34, 56);
	}

	@Test
	public void testUpdateVisibility() {
		// given
		Mockito.when(this.renderArea.getPlatformRenderArea()).thenReturn(
				this.platformRenderArea);
		// when
		this.renderAreaGeoExecutor.updateVisibility(false);

		// then
		Mockito.verify(this.platformRenderArea, Mockito.times(1)).hide();
	}

}
