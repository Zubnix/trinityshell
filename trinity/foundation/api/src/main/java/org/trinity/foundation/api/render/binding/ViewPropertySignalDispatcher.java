package org.trinity.foundation.api.render.binding;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.trinity.foundation.api.render.binding.refactor.model.ViewProperty;
import org.trinity.foundation.api.render.binding.refactor.model.ViewPropertyChanged;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;

import com.google.inject.Inject;

/***************************************
 * Post processes a method annotated with {@link ViewPropertyChanged}. It reads
 * the {@link ViewPropertyChanged#value()} and finds for each String value a
 * {@link ViewProperty} and {@link PropertySlot} with the same name. Each
 * matching {@code ViewProperty} will be read and it's corresponding
 * {@code PropertySlot} will be invoked. Invoking the
 * {@code PropertySlot} is done through an underlying
 * {@link ViewSlotHandler} implementation.
 * <p>
 * This class is used by Google Guice AOP.
 *************************************** 
 */
public class ViewPropertySignalDispatcher implements MethodInterceptor {

	private BindingDiscovery bindingDiscovery;

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object invocationResult = invocation.proceed();

		final Object thisObj = invocation.getThis();
		final Class<?> thisObjClass = invocation.getMethod().getDeclaringClass();

		final ViewPropertyChanged viewSignal = invocation.getMethod().getAnnotation(ViewPropertyChanged.class);
		final String[] viewPropertyNames = viewSignal.value();

		getViewPropertyDiscovery().notifyViewPropertySlot(	thisObjClass,
															thisObj,
															viewPropertyNames);
		return invocationResult;
	}

	public BindingDiscovery getViewPropertyDiscovery() {
		return this.bindingDiscovery;
	}

	@Inject
	public void setViewPropertyDiscovery(final BindingDiscovery bindingDiscovery) {
		this.bindingDiscovery = bindingDiscovery;
	}
}