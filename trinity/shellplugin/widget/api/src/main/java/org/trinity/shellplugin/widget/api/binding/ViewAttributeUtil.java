package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ViewAttributeUtil {
	// yo dawg, I heard you like caches so I put a cache in your cache so you
	// can cache while you cache!
	private static final Cache<Class<?>, Cache<String, Field[]>> viewAttributeFields = CacheBuilder.newBuilder()
			.build();
	private static final Cache<Class<?>, Field> viewReferenceFields = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Method> viewSlots = CacheBuilder.newBuilder().build();

	private ViewAttributeUtil() {

	}

	public static Field[] lookupViewAttributeFields(final Class<?> thisObjClass,
													final String viewAttributeName) throws ExecutionException {
		return ViewAttributeUtil.viewAttributeFields.get(	thisObjClass,
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

	private static Field[] getViewAttributeFields(	final Class<?> clazz,
													final String viewAttributeName) {

		final List<Field> allViewAttributeFields = new ArrayList<Field>();
		Class<?> searchedClass = clazz;
		while (searchedClass != null) {
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
			allViewAttributeFields.addAll(viewAttributeFields);
			searchedClass = searchedClass.getSuperclass();
		}

		if (allViewAttributeFields.isEmpty()) {
			throw new IllegalArgumentException(String.format(	"Found no %s with name: %s on %s",
																ViewAttribute.class.getName(),
																viewAttributeName,
																clazz.getName()));
		}
		return allViewAttributeFields.toArray(new Field[] {});
	}

	private static boolean isDuplicateViewAttribute(final List<ViewAttribute> viewAttributes,
													final ViewAttribute viewAttribute) {
		for (final ViewAttribute addedViewAttribute : viewAttributes) {
			if (addedViewAttribute.id().equals(viewAttribute.id())) {
				return true;
			}
		}
		return false;
	}

	public static Field lookupViewReferenceField(final Class<?> clazz) throws ExecutionException {

		return ViewAttributeUtil.viewReferenceFields.get(	clazz,
															new Callable<Field>() {
																@Override
																public Field call() {
																	Field foundField = null;
																	final Field[] declaredFields = clazz
																			.getDeclaredFields();
																	for (final Field field : declaredFields) {
																		final ViewReference viewReference = field
																				.getAnnotation(ViewReference.class);
																		if ((viewReference != null)
																				&& (foundField != null)) {
																			throw new IllegalArgumentException(String
																					.format("Found multiple %s on %s",
																							ViewReference.class
																									.getName(),
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

	public static Method lookupViewSlot(final Class<?> clazz,
										final String viewAttributeName) throws ExecutionException {
		return ViewAttributeUtil.viewSlots.get(	clazz,
												new Callable<Method>() {
													@Override
													public Method call() {
														Method foundMethod = null;

														for (final Method method : clazz.getMethods()) {

															final ViewAttributeSlot viewSlot = method
																	.getAnnotation(ViewAttributeSlot.class);

															if (viewSlot == null) {
																continue;
															}

															final String[] values = viewSlot.value();
															if (Arrays.asList(values).contains(viewAttributeName)) {
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
