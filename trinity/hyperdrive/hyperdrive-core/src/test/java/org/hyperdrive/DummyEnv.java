package org.hyperdrive;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.hydrogen.api.display.Display;
import org.hydrogen.api.display.DisplayPlatform;
import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.PlatformRenderAreaAttributes;
import org.hydrogen.api.display.PlatformRenderAreaGeometry;
import org.hydrogen.api.display.ResourceHandle;
import org.hydrogen.api.geometry.Coordinates;
import org.hydrogen.api.geometry.Rectangle;
import org.hydrogen.api.paint.PaintCall;
import org.hydrogen.api.paint.PaintContext;
import org.hydrogen.api.paint.Paintable;
import org.hydrogen.api.paint.PaintableRef;
import org.hydrogen.api.paint.Painter;
import org.hydrogen.api.paint.PainterFactory;
import org.hydrogen.api.paint.PainterFactoryProvider;
import org.hyperdrive.api.widget.PaintInstruction;
import org.hyperdrive.api.widget.Root;
import org.hyperdrive.api.widget.ViewBinder;
import org.hyperdrive.api.widget.Widget;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DummyEnv {

	// displayplatform level:
	@Mock
	public DisplayPlatform displayPlatformMock;
	@Mock
	public PainterFactoryProvider painterFactoryProviderMock;

	// display level:
	public String dislayNameMock = "display mock";
	@Mock
	public Display displayMock;
	@Mock
	public PainterFactory painterFactoryMock;

	// client level:
	@Mock
	public PlatformRenderArea c0;
	@Mock
	public PlatformRenderArea c1;
	@Mock
	public PlatformRenderAreaGeometry c0PlatformRenderAreaGeometry;
	@Mock
	public PlatformRenderAreaAttributes c0PlatformRenderAreaAttributes;
	@Mock
	public PlatformRenderAreaGeometry c1PlatformRenderAreaGeometry;
	@Mock
	public PlatformRenderAreaAttributes c1PlatformRenderAreaAttributes;

	// widget level:
	@Mock
	public Widget widgetMock;
	@Mock
	public ResourceHandle widgetResourceHandleMock;
	@Mock
	public PlatformRenderArea widgetPlatformRenderAreaMock;
	@Mock
	public Painter painterMock;
	@Mock
	public PaintContext widgetPaintContextMock;

	// widget view level:
	@Mock
	public Widget.View widgetViewMock;
	@Mock
	public Object widgetPaintPeer;
	@Mock
	public static PaintCall nullPaintCallMock;
	@Mock
	public static PaintCall createPaintCallMock;
	@InjectMocks
	public static PaintInstruction destroyInstructionMock = new PaintInstruction(
			DummyEnv.nullPaintCallMock);
	@Mock
	public static PaintInstruction createInstructionMock;
	@Mock
	public static Future<ResourceHandle> createInstructionFutureMock;
	@Mock
	public PaintableRef widgetPaintRef;

	@Mock
	public PlatformRenderAreaGeometry widgetPlatformRenderAreaGeometry;

	@Mock
	public PlatformRenderAreaAttributes widgetPlatformRenderAreaAttributes;

	@Mock
	public Coordinates positionMock;

	public void setup() {
		MockitoAnnotations.initMocks(this);
		stubAll();
	}

	public void stubAll() {

		// stub painterfactory provider
		Mockito.when(
				this.painterFactoryProviderMock
						.newPainterFactory(this.displayMock)).thenReturn(
				this.painterFactoryMock);

		// stub display platform
		Mockito.when(this.displayPlatformMock.newDisplay(this.dislayNameMock))
				.thenReturn(this.displayMock);
		Mockito.when(this.displayPlatformMock.getPainterFactoryProvider())
				.thenReturn(this.painterFactoryProviderMock);
		Mockito.when(
				this.displayPlatformMock.findPaintablePlatformRenderAreaFromId(
						this.displayMock, this.widgetResourceHandleMock))
				.thenReturn(this.widgetPlatformRenderAreaMock);
		Mockito.when(
				this.displayPlatformMock.findPaintablePlatformRenderAreaFromId(
						this.displayMock, this.widgetResourceHandleMock))
				.thenReturn(this.widgetPlatformRenderAreaMock);

		// stub display
		Mockito.when(this.displayMock.getDisplayName()).thenReturn(
				this.dislayNameMock);
		Mockito.when(this.displayMock.getDisplayPlatform()).thenReturn(
				this.displayPlatformMock);

		// stub painterfactory
		Mockito.when(
				this.painterFactoryMock.getNewPainter((Paintable) Matchers
						.any())).thenReturn(this.painterMock);
		Mockito.when(this.displayMock.getPainterFactory()).thenReturn(
				this.painterFactoryMock);

		// stub client platformrenderareas
		Mockito.when(this.c0.getPlatformRenderAreaGeometry()).thenReturn(
				this.c0PlatformRenderAreaGeometry);
		Mockito.when(this.c0.getPlatformRenderAreaAttributes()).thenReturn(
				this.c0PlatformRenderAreaAttributes);
		Mockito.when(this.c1.getPlatformRenderAreaGeometry()).thenReturn(
				this.c1PlatformRenderAreaGeometry);
		Mockito.when(this.c1.getPlatformRenderAreaAttributes()).thenReturn(
				this.c1PlatformRenderAreaAttributes);

		// stub clients platformrenderarea geometry
		Mockito.when(this.c0PlatformRenderAreaGeometry.getRelativeX())
				.thenReturn(5);
		Mockito.when(this.c0PlatformRenderAreaGeometry.getRelativeY())
				.thenReturn(5);
		Mockito.when(this.c0PlatformRenderAreaGeometry.getWidth()).thenReturn(
				50);
		Mockito.when(this.c0PlatformRenderAreaGeometry.getHeight()).thenReturn(
				50);
		Mockito.when(this.c1PlatformRenderAreaGeometry.getRelativeX())
				.thenReturn(0);
		Mockito.when(this.c1PlatformRenderAreaGeometry.getRelativeY())
				.thenReturn(0);
		Mockito.when(this.c1PlatformRenderAreaGeometry.getWidth()).thenReturn(
				75);
		Mockito.when(this.c1PlatformRenderAreaGeometry.getHeight()).thenReturn(
				75);

		// stub clients platformrenderarea attributes
		Mockito.when(this.c0PlatformRenderAreaAttributes.isViewable())
				.thenReturn(true);
		Mockito.when(this.c1PlatformRenderAreaAttributes.isViewable())
				.thenReturn(true);

		// stub widget view
		Mockito.when(this.widgetViewMock.doCreate(null, true, null))
				.thenReturn(DummyEnv.createInstructionMock);
		Mockito.when(this.widgetViewMock.doDestroy()).thenReturn(
				DummyEnv.destroyInstructionMock);

		// stub painter
		Mockito.when(this.painterMock.getPaintable()).thenReturn(
				this.widgetMock);
		Mockito.when(this.painterMock.paint(DummyEnv.createPaintCallMock))
				.thenReturn(DummyEnv.createInstructionFutureMock);

		// stub widget
		Mockito.when(this.widgetMock.getView()).thenReturn(this.widgetViewMock);
		Mockito.when(this.widgetMock.getPlatformRenderArea()).thenReturn(
				this.widgetPlatformRenderAreaMock);

		ViewBinder.bindView(Widget.View.class, WidgetViewMock.class);
		ViewBinder.bindView(Root.View.class, WidgetViewMock.class);

		// stub widget platform renderarea
		Mockito.when(
				this.widgetPlatformRenderAreaMock
						.getPlatformRenderAreaGeometry()).thenReturn(
				this.widgetPlatformRenderAreaGeometry);
		Mockito.when(
				this.widgetPlatformRenderAreaMock
						.getPlatformRenderAreaAttributes()).thenReturn(
				this.widgetPlatformRenderAreaAttributes);
		Mockito.when(
				this.widgetPlatformRenderAreaMock.translateCoordinates(
						(PlatformRenderArea) Matchers.any(), Matchers.anyInt(),
						Matchers.anyInt())).thenReturn(this.positionMock);

		// stub widget platformrenderarea geometry
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getRelativeX())
				.thenReturn(0);
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getRelativeY())
				.thenReturn(0);
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getWidth())
				.thenReturn(100);
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getHeight())
				.thenReturn(100);

		// stub widget platformrenderarea attributes
		Mockito.when(this.widgetPlatformRenderAreaAttributes.isViewable())
				.thenReturn(true);

		// stub paintcontext
		Mockito.when(this.widgetPaintContextMock.getPaintableRef()).thenReturn(
				this.widgetPaintRef);
		Mockito.when(this.widgetPaintContextMock.getPaintPeer()).thenReturn(
				this.widgetPaintPeer);

		// stub create paint call
		Mockito.when(
				DummyEnv.createPaintCallMock.call(this.widgetPaintContextMock))
				.thenReturn(this.widgetResourceHandleMock);

		// stub create paint instruction
		Mockito.when(DummyEnv.createInstructionMock.getPaintCall()).thenReturn(
				DummyEnv.createPaintCallMock);
		Mockito.when(DummyEnv.createInstructionMock.getPaintResult())
				.thenReturn(DummyEnv.createInstructionFutureMock);

		// stub create instruction future
		try {
			Mockito.when(DummyEnv.createInstructionFutureMock.get())
					.thenReturn(this.widgetResourceHandleMock);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class WidgetViewMock implements Widget.View {

		@Override
		public PaintInstruction<ResourceHandle> doCreate(final Rectangle form,
				final boolean visible, final Paintable parentPaintable) {
			return DummyEnv.createInstructionMock;
		}

		@Override
		public PaintInstruction<Void> doDestroy() {
			return DummyEnv.destroyInstructionMock;
		}
	}
}
