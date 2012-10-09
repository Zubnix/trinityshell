package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.inject.Inject;

public class ViewAttributeSignalDispatcher implements MethodInterceptor {

	@Inject
	private ViewSlotInvocationHandler viewSlotInvocationHandler;

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object invocationResult = invocation.proceed();

		final Object thisObj = invocation.getThis();
		final Class<?> thisObjClass = invocation.getMethod().getDeclaringClass();

		final ViewAttributeChanged viewSignal = invocation.getMethod().getAnnotation(ViewAttributeChanged.class);
		final String[] viewAttributeIds = viewSignal.value();

		final Field viewField = ViewAttributeUtil.lookupViewReferenceField(thisObjClass);
		viewField.setAccessible(true);
		final Object view = viewField.get(thisObj);
		viewField.setAccessible(false);

		for (final String viewAttributeName : viewAttributeIds) {
			final Field[] fields = ViewAttributeUtil.lookupViewAttributeFields(	thisObjClass,
																				viewAttributeName);

			final Method viewSlotMethod = ViewAttributeUtil.lookupViewSlot(	view.getClass(),
																			viewAttributeName);
			if (viewSlotMethod == null) {
				continue;
			}

			for (final Field field : fields) {
				field.setAccessible(true);
				final Object argument = field.get(thisObj);
				field.setAccessible(false);
				this.viewSlotInvocationHandler.invokeSlot(	(PaintableSurfaceNode) thisObj,
															field.getAnnotation(ViewAttribute.class),
															view,
															viewSlotMethod,
															argument);
			}
		}
		return invocationResult;
	}

}