// package org.trinity.shell.api.widget;
//
// import java.util.concurrent.ExecutionException;
//
// import org.junit.Test;
// import org.mockito.Matchers;
// import org.mockito.Mockito;
// import org.trinity.foundation.api.display.server.DisplaySurface;
// import org.trinity.foundation.api.render.Painter;
// import org.trinity.foundation.api.render.client.PainterProxyFactory;
// import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
//
// import com.google.common.util.concurrent.ListenableFuture;
//
// public class BaseShellWidgetTest {
//
// @Test
// public void testInit() throws InterruptedException, ExecutionException {
// // given
// final ShellDisplayEventDispatcher shellDisplayEventDispatcher =
// Mockito.mock(ShellDisplayEventDispatcher.class);
// final PainterProxyFactory painterProxyFactory =
// Mockito.mock(PainterProxyFactory.class);
// final BaseShellWidget parentWidget = Mockito.mock(BaseShellWidget.class);
// final DisplaySurface displaySurface = Mockito.mock(DisplaySurface.class);
// final ListenableFuture<DisplaySurface> listenableFuture =
// Mockito.mock(ListenableFuture.class);
// Mockito.when(listenableFuture.get()).thenReturn(displaySurface);
// Mockito.when(parentWidget.getDisplaySurface()).thenReturn(listenableFuture);
// final Painter painter = Mockito.mock(Painter.class);
// Mockito.when(painterProxyFactory.createPainter(Matchers.any())).thenReturn(painter);
// final BaseShellWidget baseShellWidget = new
// BaseShellWidget(shellDisplayEventDispatcher,
// painterProxyFactory);
//
// Mockito.when(painter.getDislaySurface()).thenReturn(listenableFuture);
//
// baseShellWidget.setX(50);
// baseShellWidget.setY(75);
// baseShellWidget.setWidth(100);
// baseShellWidget.setHeight(200);
//
// // when
// baseShellWidget.setParent(parentWidget);
// baseShellWidget.doReparent();
//
// // then
// Mockito.verify( painter,
// Mockito.times(1)).moveResize( 50,
// 75,
// 100,
// 200);
//
// }
// }
