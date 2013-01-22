package org.trinity.shell.api.widget;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;

public class BaseShellWidgetTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testInit() {
		// given
		final EventBus eventBus = mock(EventBus.class);
		final ShellDisplayEventDispatcher shellDisplayEventDispatcher = mock(ShellDisplayEventDispatcher.class);
		final PainterFactory painterFactory = mock(PainterFactory.class);
		final BaseShellWidget parentWidget = mock(BaseShellWidget.class);
		final DisplaySurface displaySurface = mock(DisplaySurface.class);
		when(parentWidget.getDisplaySurface()).thenReturn(Optional.of(displaySurface));
		final Painter painter = mock(Painter.class);
		when(painterFactory.createPainter(any())).thenReturn(painter);
		final BaseShellWidget baseShellWidget = new BaseShellWidget(eventBus,
																	shellDisplayEventDispatcher,
																	painterFactory);

		when(painter.getDislaySurface()).thenReturn(Optional.<DisplaySurface> absent(),
													Optional.<DisplaySurface> of(displaySurface));

		baseShellWidget.setX(50);
		baseShellWidget.setY(75);
		baseShellWidget.setWidth(100);
		baseShellWidget.setHeight(200);

		// when
		baseShellWidget.setParent(parentWidget);
		baseShellWidget.doReparent();

		// then
		verify(	painter,
				times(1)).moveResize(	50,
										75,
										100,
										200);

	}
}
