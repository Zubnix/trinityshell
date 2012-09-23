package org.trinity.render.swt.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.swt.api.SwtPaintContext;
import org.trinity.render.swt.api.SwtRenderEngine;
import org.trinity.shellplugin.widget.api.mvvm.ViewSlotInvocationHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ViewSlotInvocationHandlerImpl implements ViewSlotInvocationHandler {

	private final SwtRenderEngine renderEngine;

	@Inject
	ViewSlotInvocationHandlerImpl(final SwtRenderEngine qjRenderEngine) {
		this.renderEngine = qjRenderEngine;
	}

	@Override
	public void invoke(final PaintableSurfaceNode paintableSurfaceNode,
			final Object view, final Method viewMethod,
			final Object viewMethodArgument) {
		this.renderEngine.invoke(paintableSurfaceNode,
				new PaintInstruction<Void, SwtPaintContext>() {
					@Override
					public Void call(
							final PaintableSurfaceNode paintableSurfaceNode,
							final SwtPaintContext paintContext) {
						try {
							viewMethod.invoke(view, paintableSurfaceNode,
									paintContext, viewMethodArgument);
							return null;
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							throw new Error(e);
						}
					};
				});
	}
}