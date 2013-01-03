package org.trinity.render.qt.lnf.impl.binding;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.render.qt.api.QJRenderEngine;
import org.trinity.render.qt.lnf.impl.DummyView;
import org.trinity.render.qt.lnf.impl.binding.ViewSlotInvocationHandlerImpl;
import org.trinity.shellplugin.widget.api.binding.ViewProperty;

public class ViewSlotInvocationHandlerImplTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() throws NoSuchMethodException, SecurityException {

		final QJRenderEngine qjRenderEngine = mock(QJRenderEngine.class);

		final PaintableSurfaceNode paintableSurfaceNode = mock(PaintableSurfaceNode.class);
		final ViewProperty viewProperty = mock(ViewProperty.class);
		final DummyView view = mock(DummyView.class);
		final Method viewAttributeSlot = DummyView.class.getMethod(	"dummyViewAttributeSlot",
																	ViewProperty.class,
																	PaintableSurfaceNode.class,
																	QJPaintContext.class);
		final Object viewAttributeValue = mock(Object.class);

		final ViewSlotInvocationHandlerImpl viewSlotInvocationHandlerImpl = new ViewSlotInvocationHandlerImpl(qjRenderEngine);
		viewSlotInvocationHandlerImpl.invokeSlot(	paintableSurfaceNode,
													viewProperty,
													view,
													viewAttributeSlot,
													viewAttributeValue);

		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<PaintInstruction> paintInstructionCaptor = ArgumentCaptor.forClass(PaintInstruction.class);
		verify(qjRenderEngine).invoke(	eq(paintableSurfaceNode),
										paintInstructionCaptor.capture());

		final QJPaintContext paintContext = mock(QJPaintContext.class);

		paintInstructionCaptor.getValue().call(paintContext);

		verify(view).dummyViewAttributeSlot(viewProperty,
											null,
											paintContext);
	}
}
