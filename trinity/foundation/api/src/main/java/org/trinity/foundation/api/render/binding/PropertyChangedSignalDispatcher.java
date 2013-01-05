package org.trinity.foundation.api.render.binding;

import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.render.binding.view.PropertySlot;

import com.google.inject.Inject;

/***************************************
 * Post processes a method annotated with {@link PropertyChanged}. It reads the
 * {@link PropertyChanged#value()} and finds for each String value a
 * {@link ViewProperty} and {@link PropertySlot} with the same name. Each
 * matching {@code ViewProperty} will be read and it's corresponding
 * {@code PropertySlot} will be invoked. Invoking the {@code PropertySlot} is
 * done through an underlying {@link ViewSlotHandler} implementation.
 * <p>
 * This class is used by Google Guice AOP.
 *************************************** 
 */
public class PropertyChangedSignalDispatcher implements MethodInterceptor {

	private Binder binder;

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object invocationResult = invocation.proceed();

		final Object changedModel = invocation.getThis();
		final Class<?> changedModelClass = invocation.getMethod().getDeclaringClass();

		final PropertyChanged changedPropertySignal = invocation.getMethod().getAnnotation(PropertyChanged.class);
		final String[] changedPropertyNames = changedPropertySignal.value();

		for (final String propertyName : changedPropertyNames) {
			final Object propertyInstance = getBinder().findGetter(	changedModelClass,
																	propertyName).invoke(changedModel);
			final Set<ModelBinding> dirtyBindings = getBinder().getViewElements(changedModel);
			for (final ModelBinding dirtyBinding : dirtyBindings) {

			}
		}

		return invocationResult;
	}

	public Binder getBinder() {
		return this.binder;
	}

	@Inject
	void setBinder(final Binder binder) {
		this.binder = binder;
	}
}