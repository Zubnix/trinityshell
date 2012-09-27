package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.Method;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

public interface ViewAttributeSlotInvocationHandler {

	void invokeSlot(PaintableSurfaceNode paintableSurfaceNode,
				ViewAttribute viewAttribute,
				Object view,
				Method viewSlot,
				Object argument);
}
