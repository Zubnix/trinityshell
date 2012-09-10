package org.trinity.shellplugin.widget.api.mvvm;

import java.lang.reflect.Method;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

public interface ViewSlotInvocationHandler {

	void invoke(PaintableSurfaceNode paintableSurfaceNode,
				Object view,
				Method viewSlot,
				Object argument);
}
