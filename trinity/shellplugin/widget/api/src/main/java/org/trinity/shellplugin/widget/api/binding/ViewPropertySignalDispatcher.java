package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.inject.Inject;

/***************************************
 * Post processes a method annotated with {@link ViewPropertyChanged}. It reads
 * the {@link ViewPropertyChanged#value()} and finds for each String value a
 * {@link ViewProperty} and {@link ViewPropertySlot} with the same name. Each
 * matching {@code ViewProperty} will be read and it's corresponding
 * {@code ViewPropertySlot} will be invoked. Invoking the
 * {@code ViewPropertySlot} is done through an underlying
 * {@link ViewSlotInvocationHandler} implementation.
 * <p>
 * This class is used by Google Guice AOP.
 *************************************** 
 */
public class ViewPropertySignalDispatcher implements MethodInterceptor {

	@Inject
	private ViewSlotInvocationHandler viewSlotInvocationHandler;

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object invocationResult = invocation.proceed();

		final Object thisObj = invocation.getThis();
		final Class<?> thisObjClass = invocation.getMethod().getDeclaringClass();

		final ViewPropertyChanged viewSignal = invocation.getMethod().getAnnotation(ViewPropertyChanged.class);
		final String[] viewAttributeIds = viewSignal.value();

		final Field viewField = ViewPropertyUtil.lookupViewReferenceField(thisObjClass);
		viewField.setAccessible(true);
		final Object view = viewField.get(thisObj);
		viewField.setAccessible(false);

		for (final String viewAttributeName : viewAttributeIds) {
			// FIXME only allow to find one field.
			final Field[] fields = ViewPropertyUtil.lookupViewAttributeFields(	thisObjClass,
																				viewAttributeName);
			// TODO also search for property getter & make sure only one has a
			// result.

			final Method viewSlotMethod = ViewPropertyUtil.lookupViewSlot(	view.getClass(),
																			viewAttributeName);
			if (viewSlotMethod == null) {
				continue;
			}

			for (final Field field : fields) {
				field.setAccessible(true);
				final Object argument = field.get(thisObj);
				field.setAccessible(false);
				this.viewSlotInvocationHandler.invokeSlot(	(PaintableSurfaceNode) thisObj,
															field.getAnnotation(ViewProperty.class),
															view,
															viewSlotMethod,
															argument);
			}
		}
		return invocationResult;
	}

}