package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;

final class ViewAttributeSignalDispatcher implements MethodInterceptor {

	private final Cache<Class<?>, Cache<String, Field>> visualReferenceFieldBindings = CacheBuilder.newBuilder()
			.build();
	private final Cache<Class<?>, Field> viewReferenceFieldBindings = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Method> viewSlotMethodBindings = CacheBuilder.newBuilder().build();

	@Inject
	private ViewAttributeSlotInvocationHandler viewSlotInvocationHandler;

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object invocationResult = invocation.proceed();

		final Object thisObj = invocation.getThis();
		final Class<?> thisObjClass = ((Method) invocation.getStaticPart()).getDeclaringClass();

		final ViewAttributeChanged viewSignal = invocation.getMethod().getAnnotation(ViewAttributeChanged.class);
		final String[] viewAttributeIds = viewSignal.value();

		final Field viewField = getViewReferenceField(thisObjClass);
		viewField.setAccessible(true);
		final Object view = viewField.get(thisObj);
		viewField.setAccessible(false);

		for (final String viewAttributeId : viewAttributeIds) {
			final Field field = this.visualReferenceFieldBindings.get(	thisObjClass,
																		new Callable<Cache<String, Field>>() {
																			@Override
																			public Cache<String, Field> call() {
																				return CacheBuilder.newBuilder()
																						.build();
																			}
																		}).get(	viewAttributeId,
																				new Callable<Field>() {
																					@Override
																					public Field call() {
																						return getVisualReferenceField(	thisObjClass,
																														viewAttributeId);
																					}
																				});

			final Method viewSlotMethod = getViewSlotMethod(view.getClass(),
															viewAttributeId);
			if (viewSlotMethod == null) {
				continue;
			}

			field.setAccessible(true);
			final Object argument = field.get(thisObj);
			field.setAccessible(false);
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
			final ViewAttribute visual = field.getAnnotation(ViewAttribute.class);
			if ((visual != null) && visual.value().equals(visualId)) {
				if (foundField == null) {
					foundField = field;
				} else {
					throw new IllegalArgumentException(String.format(	"Found multiple %s with value: %s on %s",
																		ViewAttribute.class.getName(),
																		visualId,
																		clazz.getName()));
				}
			}
		}
		if (foundField == null) {
			throw new IllegalArgumentException(String.format(	"Found no %s with value: %s on %s",
																ViewAttribute.class.getName(),
																visualId,
																clazz.getName()));
		}
		return foundField;
	}

	private Field getViewReferenceField(final Class<?> clazz) throws ExecutionException {

		return this.viewReferenceFieldBindings.get(	clazz,
													new Callable<Field>() {
														@Override
														public Field call() {
															Field foundField = null;
															final Field[] declaredFields = clazz.getDeclaredFields();
															for (final Field field : declaredFields) {
																final ViewReference viewReference = field
																		.getAnnotation(ViewReference.class);
																if ((viewReference != null) && (foundField != null)) {
																	throw new IllegalArgumentException(String
																			.format("Found multiple %s on %s",
																					ViewReference.class.getName(),
																					clazz.getName()));
																} else if (viewReference != null) {
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
													public Method call() {
														Method foundMethod = null;

														for (final Method method : clazz.getDeclaredMethods()) {

															final ViewAttributeSlot viewSlot = method
																	.getAnnotation(ViewAttributeSlot.class);

															if (viewSlot == null) {
																continue;
															}

															final String[] values = viewSlot.value();
															if (Arrays.asList(values).contains(visualId)) {
																if ((foundMethod == null)) {
																	foundMethod = method;
																} else if ((foundMethod != null)) {
																	throw new IllegalArgumentException(String
																			.format("Found multiple %s on %s",
																					ViewAttributeSlot.class.getName(),
																					clazz.getName()));
																}
															}
														}

														return foundMethod;
													}
												});
	}
}