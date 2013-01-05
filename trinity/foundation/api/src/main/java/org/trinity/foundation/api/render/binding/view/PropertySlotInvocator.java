package org.trinity.foundation.api.render.binding.view;

import java.lang.reflect.Method;

public interface PropertySlotInvocator {
	void invoke(Object view,
				Method viewMethod,
				Object argument);
}
