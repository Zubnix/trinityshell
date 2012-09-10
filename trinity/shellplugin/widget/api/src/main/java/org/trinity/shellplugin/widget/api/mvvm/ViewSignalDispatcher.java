package org.trinity.shellplugin.widget.api.mvvm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;

final class ViewSignalDispatcher implements MethodInterceptor {

	private final Cache<Class<?>, Cache<String, Field>> visualReferenceFieldBindings = CacheBuilder.newBuilder()
			.build();
	private final Cache<Class<?>, Field> viewReferenceFieldBindings = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Method> viewSlotMethodBindings = CacheBuilder.newBuilder().build();

	@Inject
	private ViewSlotInvocationHandler viewSlotInvocationHandler;

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object invocationResult = invocation.proceed();

		final Object thisObj = invocation.getThis();
		final Class<?> thisObjClass = thisObj.getClass();

		final ViewSignal viewSignal = invocation.getMethod().getAnnotation(ViewSignal.class);
		final String[] visualIds = viewSignal.value();

		final Field viewField = getViewReferenceField(thisObjClass);
		final Object view = viewField.get(thisObj);

		for (final String visualId : visualIds) {
			final Field field = this.visualReferenceFieldBindings.get(	thisObjClass,
																		new Callable<Cache<String, Field>>() {
																			@Override
																			public Cache<String, Field> call()
																					throws Exception {
																				return CacheBuilder.newBuilder()
																						.build();
																			}
																		}).get(	visualId,
																				new Callable<Field>() {
																					@Override
																					public Field call()
																							throws Exception {
																						return getVisualReferenceField(	thisObjClass,
																														visualId);
																					}
																				});

			final Method viewSlotMethod = getViewSlotMethod(view.getClass(),
															visualId);
			if (viewSlotMethod == null) {
				continue;
			}

			final Object argument = field.get(thisObj);
			this.viewSlotInvocationHandler.invoke(	(PaintableSurfaceNode) thisObj,
													view,
													viewSlotMethod,
													argument);
		}
		return invocationResult;
	}

	private Field getVisualReferenceField(	final Class<?> clazz,
											final String visualId) {
		Field foundField = null;
		for (final Field field : clazz.getDeclaredFields()) {
			final VisualReference visual = field.getAnnotation(VisualReference.class);
			if ((visual != null) && visual.value().equals(visualId)) {
				if (foundField == null) {
					foundField = field;
				} else {
					throw new IllegalArgumentException(String.format(	"Found multiple %s with value: %s on %s",
																		VisualReference.class.getName(),
																		visualId,
																		clazz.getName()));
				}
			}
		}
		if (foundField == null) {
			throw new IllegalArgumentException(String.format(	"Found no %s with value: %s on %s",
																VisualReference.class.getName(),
																visualId,
																clazz.getName()));
		}
		return foundField;
	}

	private Field getViewReferenceField(final Class<?> clazz) throws ExecutionException {

		return this.viewReferenceFieldBindings.get(	clazz,
													new Callable<Field>() {
														@Override
														public Field call() throws Exception {
															Field foundField = null;
															for (final Field field : clazz.getDeclaredFields()) {
																final VisualReference visualReference = field
																		.getAnnotation(VisualReference.class);
																if ((visualReference != null) && (foundField != null)) {
																	throw new IllegalArgumentException(String
																			.format("Found multiple %s on %s",
																					ViewReference.class.getName(),
																					clazz.getName()));
																} else if (visualReference != null) {
																	foundField = field;
																}
															}
															if (foundField == null) {
																throw new IllegalArgumentException(String
																		.format("Found no %s on %s",
																				ViewReference.class.getName(),
																				clazz.getName()));
															}

															return foundField;
														}
													});
	}

	private Method getViewSlotMethod(	final Class<?> clazz,
										final String visualId) throws ExecutionException {
		return this.viewSlotMethodBindings.get(	clazz,
												new Callable<Method>() {
													@Override
													public Method call() throws Exception {
														Method foundMethod = null;
														for (final Method method : clazz.getDeclaredMethods()) {
															final ViewSlot viewSlot = method
																	.getAnnotation(ViewSlot.class);
															if ((viewSlot != null) && viewSlot.value().equals(visualId)) {
																if ((foundMethod == null)) {
																	foundMethod = method;
																} else if ((foundMethod != null)) {
																	throw new IllegalArgumentException(String
																			.format("Found multiple %s on %s",
																					ViewSlot.class.getName(),
																					clazz.getName()));
																}
															}
														}

														return foundMethod;
													}
												});
	}
}