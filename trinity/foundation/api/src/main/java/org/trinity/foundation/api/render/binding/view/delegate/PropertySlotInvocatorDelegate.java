package org.trinity.foundation.api.render.binding.view.delegate;

import java.lang.reflect.Method;

public interface PropertySlotInvocatorDelegate {
	void invoke(Object view,
				Method viewMethod,
				Object argument);
}
