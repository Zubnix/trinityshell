package org.hyperdrive;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.hydrogen.display.api.Atom;
import org.hydrogen.display.api.Display;
import org.hydrogen.display.api.DisplayAtoms;
import org.hydrogen.display.api.DisplayPlatform;
import org.hydrogen.display.api.PlatformRenderArea;
import org.hydrogen.display.api.PlatformRenderAreaAttributes;
import org.hydrogen.display.api.PlatformRenderAreaGeometry;
import org.hydrogen.display.api.Property;
import org.hydrogen.display.api.PropertyInstance;
import org.hydrogen.display.api.ResourceHandle;
import org.hydrogen.geometry.api.Coordinates;
import org.hydrogen.geometry.api.Rectangle;
import org.hydrogen.paint.api.PaintCall;
import org.hydrogen.paint.api.PaintContext;
import org.hydrogen.paint.api.Paintable;
import org.hydrogen.paint.api.PaintableRef;
import org.hydrogen.paint.api.Painter;
import org.hydrogen.paint.api.PainterFactory;
import org.hydrogen.paint.api.PainterFactoryProvider;
import org.hyperdrive.foundation.api.ManagedDisplay;
import org.hyperdrive.widget.api.Root;
import org.hyperdrive.widget.api.ViewBinder;
import org.hyperdrive.widget.api.ViewImplementation;
import org.hyperdrive.widget.api.VirtualRoot;
import org.hyperdrive.widget.api.Widget;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MockedEnv {

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
	@Mock
	public DisplayAtoms displayAtomsMock;
	public String atomMockName = "dummyAtom";
	@Mock
	public Atom atomMock;
	@Mock
	public PropertyInstance propertyInstanceMock;
	public String propertyMockName = "dummyProperty";
	@Mock
	public Property<PropertyInstance> propertyMock;

	// managed display level:
	@Mock
	public ManagedDisplay managedDisplayMock;

	// client level:
	@Mock
	public PlatformRenderArea c0;
	@Mock
	public PlatformRenderArea c1;
	@Mock
	public PlatformRenderAreaGeometry c0PlatformRenderAreaGeometry;
	public int c0X = 10;
	public int c0Y = 15;
	public int c0Width = 200;
	public int c0Height = 250;
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
	@SuppressWarnings("rawtypes")
	@Mock
	public PaintContext widgetPaintContextMock;

	// widget view level:
	@Mock
	public Widget.View widgetViewMock;
	@Mock
	public Object widgetPaintPeer;
	@SuppressWarnings("rawtypes")
	@Mock
	public static PaintCall nullPaintCallMock;
	@SuppressWarnings("rawtypes")
	@Mock
	public static PaintCall createPaintCallMock;

	@Mock
	public static Future<ResourceHandle> createInstructionFutureMock;
	@Mock
	public PaintableRef widgetPaintRef;

	@Mock
	public PlatformRenderAreaGeometry widgetPlatformRenderAreaGeometry;
	public int widgetPlatformRenderAreaX = 5;
	public int widgetPlatformRenderAreaY = 10;
	public int widgetPlatformRenderAreaWidth = 100;
	public int widgetPlatformRenderAreaHeight = 100;

	@Mock
	public PlatformRenderAreaAttributes widgetPlatformRenderAreaAttributes;

	@Mock
	public Coordinates positionMock;

	public void setUp() {
		MockitoAnnotations.initMocks(this);
		stubAll();
	}

	@SuppressWarnings("unchecked")
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
		Mockito.when(this.displayMock.getDisplayAtoms()).thenReturn(
				this.displayAtomsMock);

		// stub painterfactory
		Mockito.when(
				this.painterFactoryMock.getNewPainter((Paintable) Matchers
						.any())).thenReturn(this.painterMock);
		Mockito.when(this.displayMock.getPainterFactory()).thenReturn(
				this.painterFactoryMock);

		// stub displayatoms
		Mockito.when(this.displayAtomsMock.getAtomByName(this.atomMockName))
				.thenReturn(this.atomMock);
		Mockito.when(this.displayAtomsMock.getAtomByName(this.propertyMockName))
				.thenReturn(this.propertyMock);

		// stub atom
		Mockito.when(this.atomMock.getAtomName()).thenReturn(this.atomMockName);

		// stub property
		Mockito.when(this.propertyMock.getAtomName()).thenReturn(
				this.propertyMockName);
		Mockito.when(this.propertyMock.getDisplay()).thenReturn(
				this.displayMock);
		Mockito.when(
				this.propertyMock
						.getPropertyInstance((PlatformRenderArea) Matchers
								.any())).thenReturn(this.propertyInstanceMock);

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
				.thenReturn(this.c0X);
		Mockito.when(this.c0PlatformRenderAreaGeometry.getRelativeY())
				.thenReturn(this.c0Y);
		Mockito.when(this.c0PlatformRenderAreaGeometry.getWidth()).thenReturn(
				this.c0Width);
		Mockito.when(this.c0PlatformRenderAreaGeometry.getHeight()).thenReturn(
				this.c0Height);
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

		// stub managedDisplay
		Mockito.when(this.managedDisplayMock.getDisplay()).thenReturn(
				this.displayMock);
		Mockito.when(this.managedDisplayMock.getRoot()).thenReturn(
				this.widgetMock);

		// stub widget
		Mockito.when(this.widgetMock.getManagedDisplay()).thenReturn(
				this.managedDisplayMock);
		Mockito.when(this.widgetMock.getView()).thenReturn(this.widgetViewMock);
		Mockito.when(this.widgetMock.getPlatformRenderArea()).thenReturn(
				this.widgetPlatformRenderAreaMock);

		ViewBinder.get().bind(WidgetViewMock.class);

		// stub widget view
		Mockito.when(this.widgetViewMock.doCreate(null, true, null))
				.thenReturn(MockedEnv.createPaintCallMock);
		Mockito.when(this.widgetViewMock.doDestroy()).thenReturn(
				MockedEnv.nullPaintCallMock);

		// stub painter
		Mockito.when(this.painterMock.getPaintable()).thenReturn(
				this.widgetMock);
		Mockito.when(this.painterMock.paint(MockedEnv.createPaintCallMock))
				.thenReturn(MockedEnv.createInstructionFutureMock);

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
		Mockito.when(
				this.widgetPlatformRenderAreaMock
						.getPropertyInstance(this.propertyMock)).thenReturn(
				this.propertyInstanceMock);

		// stub widget platformrenderarea geometry
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getRelativeX())
				.thenReturn(this.widgetPlatformRenderAreaX);
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getRelativeY())
				.thenReturn(this.widgetPlatformRenderAreaY);
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getWidth())
				.thenReturn(this.widgetPlatformRenderAreaWidth);
		Mockito.when(this.widgetPlatformRenderAreaGeometry.getHeight())
				.thenReturn(this.widgetPlatformRenderAreaHeight);

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
				MockedEnv.createPaintCallMock.call(this.widgetPaintContextMock))
				.thenReturn(this.widgetResourceHandleMock);

		// stub create instruction future
		try {
			Mockito.when(MockedEnv.createInstructionFutureMock.get())
					.thenReturn(this.widgetResourceHandleMock);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@ViewImplementation({ Widget.View.class, Root.View.class,
			VirtualRoot.View.class })
	public static class WidgetViewMock implements Widget.View, Root.View,
			VirtualRoot.View {

		@Override
		public PaintCall<ResourceHandle, ?> doCreate(Rectangle form,
				boolean visible, Paintable parentPaintable) {
			return createPaintCallMock;
		}

		@Override
		public PaintCall<Void, ?> doDestroy() {
			return nullPaintCallMock;
		}
	}
}
