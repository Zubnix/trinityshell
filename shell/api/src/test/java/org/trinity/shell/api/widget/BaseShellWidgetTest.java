package org.trinity.shell.api.widget;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;

public class BaseShellWidgetTest {

	@Test
	public void testInit() {
		// given
		final ShellDisplayEventDispatcher shellDisplayEventDispatcher = Mockito.mock(ShellDisplayEventDispatcher.class);
		final PainterFactory painterFactory = Mockito.mock(PainterFactory.class);
		final BaseShellWidget parentWidget = Mockito.mock(BaseShellWidget.class);
		final DisplaySurface displaySurface = Mockito.mock(DisplaySurface.class);
		Mockito.when(parentWidget.getDisplaySurface()).thenReturn(displaySurface);
		final Painter painter = Mockito.mock(Painter.class);
		Mockito.when(painterFactory.createPainter(Matchers.any())).thenReturn(painter);
		final BaseShellWidget baseShellWidget = new BaseShellWidget(shellDisplayEventDispatcher,
																	painterFactory);

		Mockito.when(painter.getDislaySurface()).thenReturn(displaySurface);

		baseShellWidget.setX(50);
		baseShellWidget.setY(75);
		baseShellWidget.setWidth(100);
		baseShellWidget.setHeight(200);

		// when
		baseShellWidget.setParent(parentWidget);
		baseShellWidget.doReparent();

		// then
		Mockito.verify(	painter,
						Mockito.times(1)).moveResize(	50,
														75,
														100,
														200);

	}
}
