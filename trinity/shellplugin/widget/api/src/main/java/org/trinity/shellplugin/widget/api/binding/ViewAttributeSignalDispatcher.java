package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;

// TODO make this class more readable
public class ViewAttributeSignalDispatcher implements MethodInterceptor {

	// yo dawg, I heard you like caches so I put a cache in your cache so you
	// can cache while you cache!
	private final Cache<Class<?>, Cache<String, Field[]>> viewAttributeFields = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Field> viewReferenceFields = CacheBuilder.newBuilder().build();
	private final Cache<Class<?>, Method> viewSlots = CacheBuilder.newBuilder().build();

	@Inject
	private ViewAttributeSlotInvocationHandler viewSlotInvocationHandler;

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object invocationResult = invocation.proceed();

		final Object thisObj = invocation.getThis();
		final Class<?> thisObjClass = invocation.getMethod().getDeclaringClass();

		final ViewAttributeChanged viewSignal = invocation.getMethod().getAnnotation(ViewAttributeChanged.class);
		final String[] viewAttributeIds = viewSignal.value();

		final Field viewField = lookupViewReferenceFieldFromCache(thisObjClass);
		viewField.setAccessible(true);
		final Object view = viewField.get(thisObj);
		viewField.setAccessible(false);

		for (final String viewAttributeName : viewAttributeIds) {
			final Field[] fields = lookupViewAttributeFieldsFromCache(	thisObjClass,
																		viewAttributeName);

			final Method viewSlotMethod = lookupViewSlotFromCache(	view.getClass(),
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

	private Field[] lookupViewAttributeFieldsFromCache(	final Class<?> thisObjClass,
														final String viewAttributeName) throws ExecutionException {
		return this.viewAttributeFields.get(thisObjClass,
											new Callable<Cache<String, Field[]>>() {
												@Override
												public Cache<String, Field[]> call() {
													return CacheBuilder.newBuilder().build();
												}
											}).get(	viewAttributeName,
													new Callable<Field[]>() {
														@Override
														public Field[] call() {
															return getViewAttributeFields(	thisObjClass,
																							viewAttributeName);
														}
													});
	}

	private Field[] getViewAttributeFields(	final Class<?> clazz,
											final String viewAttributeName) {
		final List<Field> viewAttributeFields = new ArrayList<Field>();
		final List<ViewAttribute> viewAttributes = new ArrayList<ViewAttribute>();

		for (final Field field : clazz.getDeclaredFields()) {
			final ViewAttribute viewAttribute = field.getAnnotation(ViewAttribute.class);
			if (viewAttribute == null) {
				continue;
			}
			if (viewAttribute.name().equals(viewAttributeName)) {
				if (isDuplicateViewAttribute(	viewAttributes,
												viewAttribute)) {
					throw new IllegalArgumentException(String.format(	"Found duplicate %s with name=%s and id=%s on %s",
																		ViewAttribute.class.getName(),
																		viewAttributeName,
																		viewAttribute.id(),
																		clazz.getName()));
				}
				viewAttributes.add(viewAttribute);
				viewAttributeFields.add(field);
			}
		}

		if (viewAttributeFields.isEmpty()) {
			throw new IllegalArgumentException(String.format(	"Found no %s with name: %s on %s",
																ViewAttribute.class.getName(),
																viewAttributeName,
																clazz.getName()));
		}
		return viewAttributeFields.toArray(new Field[] {});
	}

	private boolean isDuplicateViewAttribute(	final List<ViewAttribute> viewAttributes,
												final ViewAttribute viewAttribute) {
		for (final ViewAttribute addedViewAttribute : viewAttributes) {
			if (addedViewAttribute.id().equals(viewAttribute.id())) {
				return true;
			}
		}
		return false;
	}

	private Field lookupViewReferenceFieldFromCache(final Class<?> clazz) throws ExecutionException {

		return this.viewReferenceFields.get(clazz,
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

	private Method lookupViewSlotFromCache(	final Class<?> clazz,
											final String visualId) throws ExecutionException {
		return this.viewSlots.get(	clazz,
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