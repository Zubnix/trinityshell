package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.Method;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

public interface ViewAttributeSlotInvocationHandler {

	void invoke(PaintableSurfaceNode paintableSurfaceNode,
				Object view,
				Method viewSlot,
				Object argument);
}
