package org.trinity.foundation.api.render.binding;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class ViewBindingsUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ViewBindingsUtil.class);

	private static final Table<Class<?>, String, Optional<Method>> GETTER_CACHE = HashBasedTable.create();
	private static final Table<Class<?>, String, Optional<Field>>  FIELD_CACHE  = HashBasedTable.create();
	private static final Map<Class<?>, Field[]>                    FIELDS_CACHE = Maps.newHashMap();

	private static final String GET_BOOLEAN_PREFIX = "is";
	private static final String GET_PREFIX         = "get";

	private ViewBindingsUtil() {
	}

	public static boolean appendDataModelPropertyChain(final LinkedList<DataModelProperty> dataModelPropertyChain,
													   final String propertyChain) {
		checkNotNull(dataModelPropertyChain);
		checkNotNull(propertyChain);
		checkArgument(!dataModelPropertyChain.isEmpty());

		final Iterable<String> propertyNames = toPropertyNames(propertyChain);
		final LinkedList<DataModelProperty> appendedDataModelChain = new LinkedList<>();

		boolean aborted = false;
		DataModelProperty dataModelProperty = dataModelPropertyChain.getLast();

		for(final String propertyName : propertyNames) {

			final Optional<Object> propertyValue = dataModelProperty.getPropertyValue();

			if(propertyValue.isPresent()) {
				final Object nextDataModel = propertyValue.get();
				dataModelProperty = new DataModelPropertyImpl(nextDataModel,
															  propertyName);
				appendedDataModelChain.add(dataModelProperty);
			}
			else {
				aborted = true;
				break;
			}
		}

        dataModelPropertyChain.addAll(appendedDataModelChain);
        return !aborted;
    }

    private static Iterable<String> toPropertyNames(final String subModelPath) {
        checkNotNull(subModelPath);

        return Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
    }

	public static Field[] getFields(final Class<?> viewModelClass) {
		Field[] fields = FIELDS_CACHE.get(viewModelClass);
		if(fields == null) {
			fields = viewModelClass.getDeclaredFields();
			for(final Field field : fields) {
				field.setAccessible(true);
			}
			FIELDS_CACHE.put(viewModelClass,
							 fields);
		}

		return fields;
	}

	public static Optional<Field> getField(final Class<?> viewModelClass,
										   final String subViewFieldName) {
		Optional<Field> fieldOptional = FIELD_CACHE.get(viewModelClass,
														subViewFieldName);
		if(fieldOptional == null) {

			Field foundField = null;
			try {
				foundField = viewModelClass.getDeclaredField(subViewFieldName);
				foundField.setAccessible(true);
			}
			catch(NoSuchFieldException e) {
				// TODO explanation
				LOG.error("",
						  e);
			}
			fieldOptional = Optional.fromNullable(foundField);
			FIELD_CACHE.put(viewModelClass,
							subViewFieldName,
							fieldOptional);
		}

		return fieldOptional;
	}

	public static Optional<Method> getGetterMethod(final Class<?> dataModelClass,
												   final String propertyName) {

		checkNotNull(dataModelClass);
		checkNotNull(propertyName);

		Optional<Method> methodOptional = GETTER_CACHE.get(dataModelClass,
                                                           propertyName);

        if(methodOptional == null) {
            //initialized methodOptional and store it in cache

            Method foundMethod = null;
            String getterMethodName = toGetterMethodName(propertyName);

            try {
                foundMethod = dataModelClass.getMethod(getterMethodName);
            } catch(final NoSuchMethodException e) {
                // no getter with get found,
                // try with is.
                getterMethodName = toBooleanGetterMethodName(propertyName);
                try {
                    foundMethod = dataModelClass.getMethod(getterMethodName);
                } catch(final NoSuchMethodException e1) {
                    // TODO explanation
                    LOG.error("",
                              e1);

                } catch(final SecurityException e1) {
                    LOG.error(format("Property %s is not accessible on %s. Did you declare it as public?",
                                     propertyName,
                                     dataModelClass.getName()),
                              e);
                }
            } catch(final SecurityException e1) {
                // TODO explanation
                LOG.error(format("Property %s is not accessible on %s. Did you declare it as public?",
                                 propertyName,
                                 dataModelClass.getName()),
                          e1);
            }
            methodOptional = Optional.fromNullable(foundMethod);

            GETTER_CACHE.put(dataModelClass,
                             propertyName,
                             methodOptional);
        }

        return methodOptional;
    }

    private static String toGetterMethodName(final String propertyName) {
        return toGetterMethodName(GET_PREFIX,
                                  propertyName);
    }

    private static String toGetterMethodName(final String prefix,
                                             final String propertyName) {
        checkNotNull(prefix);
        checkNotNull(propertyName);

        return prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,
                                                  propertyName);
    }

    private static String toBooleanGetterMethodName(final String propertyName) {
        return toGetterMethodName(GET_BOOLEAN_PREFIX,
                                  propertyName);
    }
}
