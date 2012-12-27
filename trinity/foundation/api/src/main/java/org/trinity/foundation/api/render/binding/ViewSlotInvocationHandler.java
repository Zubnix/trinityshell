package org.trinity.foundation.api.render.binding;

import java.lang.reflect.Method;

import org.trinity.foundation.api.render.binding.ViewProperty;
import org.trinity.foundation.api.render.binding.ViewPropertySignalDispatcher;
import org.trinity.foundation.api.render.binding.ViewPropertySlot;

/***************************************
 * Used by {@link ViewPropertySignalDispatcher} as a delegate to handle a
 * {@link ViewPropertySlot} invocation. This interface should be implemented by
 * a render back-end to handle calls to render back-end specific views.
 * 
 *************************************** 
 */
public interface ViewSlotInvocationHandler {

	/***************************************
	 * Invoke the view method to handle the changed view property.
	 * 
	 * @param dataContext
	 *            Encapsulates the changed property.
	 * @param viewProperty
	 *            The {@link ViewProperty} annotation of the property that
	 *            changed.
	 * @param view
	 *            The view.
	 * @param viewSlot
	 *            The handler method of the view that corresponds to the changed
	 *            view property.
	 * @param argument
	 *            The value of the changed property.
	 *************************************** 
	 */
	void invokeSlot(Object dataContext,
					ViewProperty viewProperty,
					Object view,
					Method viewSlot,
					Object argument);
}
