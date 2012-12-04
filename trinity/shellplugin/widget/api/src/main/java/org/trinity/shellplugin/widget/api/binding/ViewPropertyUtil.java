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

/***************************************
 * Utility class used by {@link ViewPropertySignalDispatcher} to scan for
 * {@link ViewProperty}s, {@link ViewPropertySlot}s and
 * {@link ViewPropertyChanged}s.
 *************************************** 
 */
public class ViewPropertyUtil {
	// yo dawg, I heard you like caches so I put a cache in your cache so you
	// can cache while you cache!
	private static final Cache<Class<?>, Field[]> allViewAttributeFields = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Cache<String, Field[]>> viewAttributeFields = CacheBuilder.newBuilder()
			.build();
	private static final Cache<Class<?>, Field> viewReferenceFields = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Method> viewSlots = CacheBuilder.newBuilder().build();

	private ViewPropertyUtil() {

	}

	public static Field[] lookupAllViewAttributeFields(final Class<?> thisObjClass) throws ExecutionException {
		return ViewPropertyUtil.allViewAttributeFields.get(	thisObjClass,
															new Callable<Field[]>() {
																@Override
																public Field[] call() {
																	return getAllViewAttributeFields(thisObjClass);
																}
															});
	}

	private static Field[] getAllViewAttributeFields(final Class<?> clazz) {
		final List<Field> allViewAttributeFields = new ArrayList<Field>();
		Class<?> searchedClass = clazz;
		while (searchedClass != null) {
			final List<Field> viewAttributeFields = new ArrayList<Field>();
			final List<ViewProperty> viewProperties = new ArrayList<ViewProperty>();
			for (final Field field : searchedClass.getDeclaredFields()) {
				final ViewProperty viewProperty = field.getAnnotation(ViewProperty.class);
				if (viewProperty == null) {
					continue;
				}
				if (isDuplicateViewAttribute(	viewProperties,
												viewProperty)) {
					throw new IllegalArgumentException(String.format(	"Found duplicate %s with name=%s on %s",
																		ViewProperty.class.getName(),
																		viewProperty.value(),
																		// viewProperty.id(),
																		searchedClass.getName()));
				}
				viewProperties.add(viewProperty);
				viewAttributeFields.add(field);
			}
			allViewAttributeFields.addAll(viewAttributeFields);
			searchedClass = searchedClass.getSuperclass();
		}
		return allViewAttributeFields.toArray(new Field[] {});
	}

	public static Field[] lookupViewAttributeFields(final Class<?> thisObjClass,
													final String viewAttributeName) throws ExecutionException {
		return ViewPropertyUtil.viewAttributeFields.get(thisObjClass,
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
			final List<ViewProperty> viewProperties = new ArrayList<ViewProperty>();
			for (final Field field : searchedClass.getDeclaredFields()) {
				final ViewProperty viewProperty = field.getAnnotation(ViewProperty.class);
				if (viewProperty == null) {
					continue;
				}
				if (viewProperty.value().equals(viewAttributeName)) {
					if (isDuplicateViewAttribute(	viewProperties,
													viewProperty)) {
						throw new IllegalArgumentException(String.format(	"Found duplicate %s with name=%s on %s",
																			ViewProperty.class.getName(),
																			viewAttributeName,
																			searchedClass.getName()));
					}
					viewProperties.add(viewProperty);
					viewAttributeFields.add(field);
				}
			}
			allViewAttributeFields.addAll(viewAttributeFields);
			searchedClass = searchedClass.getSuperclass();
		}

		if (allViewAttributeFields.isEmpty()) {
			throw new IllegalArgumentException(String.format(	"Found no %s with name: %s on %s",
																ViewProperty.class.getName(),
																viewAttributeName,
																clazz.getName()));
		}
		return allViewAttributeFields.toArray(new Field[] {});
	}

	private static boolean isDuplicateViewAttribute(final List<ViewProperty> viewProperties,
													final ViewProperty viewProperty) {
		for (final ViewProperty addedViewAttribute : viewProperties) {
			if (addedViewAttribute.value().equals(viewProperty.value())) {
				return true;
			}
		}
		return false;
	}

	public static Field lookupViewReferenceField(final Class<?> clazz) throws ExecutionException {

		return ViewPropertyUtil.viewReferenceFields.get(clazz,
														new Callable<Field>() {
															@Override
															public Field call() {
																Field foundField = null;
																final Field[] declaredFields = clazz
																		.getDeclaredFields();
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

	public static Method lookupViewSlot(final Class<?> clazz,
										final String viewAttributeName) throws ExecutionException {
		return ViewPropertyUtil.viewSlots.get(	clazz,
												new Callable<Method>() {
													@Override
													public Method call() {
														Method foundMethod = null;

														Class<?> searchedClass = clazz;
														while (searchedClass != null) {
															for (final Method method : searchedClass
																	.getDeclaredMethods()) {

																final ViewPropertySlot viewSlot = method
																		.getAnnotation(ViewPropertySlot.class);

																if (viewSlot == null) {
																	continue;
																}

																final String[] values = viewSlot.value();
																if (Arrays.asList(values).contains(viewAttributeName)) {
																	if ((foundMethod == null)) {
																		foundMethod = method;
																	} else if ((foundMethod != null)) {
																		throw new IllegalArgumentException(String
																				.format("Found multiple %s in the class hierarchy of %s",
																						ViewPropertySlot.class
																								.getName(),
																						clazz.getName()));
																	}
																}
															}
															searchedClass = searchedClass.getSuperclass();
														}

														return foundMethod;
													}
												});
	}
}
