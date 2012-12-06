package org.trinity.shellplugin.widget.api.binding;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
		final String[] viewPropertyNames = viewSignal.value();

		ViewPropertyUtil.notifyViewSlot(this.viewSlotInvocationHandler,
										thisObjClass,
										thisObj,
										viewPropertyNames);
		return invocationResult;
	}
}