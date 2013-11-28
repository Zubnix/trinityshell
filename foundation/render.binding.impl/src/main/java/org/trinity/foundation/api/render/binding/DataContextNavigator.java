package org.trinity.foundation.api.render.binding;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class DataContextNavigator {

    private static final Logger LOG = LoggerFactory.getLogger(DataContextNavigator.class);

    private static final Table<Class<?>, String, Optional<Method>> GETTER_CACHE = HashBasedTable.create();

    private static final String GET_BOOLEAN_PREFIX = "is";
    private static final String GET_PREFIX = "get";

    private DataContextNavigator() {
    }

    public static boolean appendDataModelPropertyChain(final LinkedList<DataModelProperty> dataModelPropertyChain,
                                                       final String propertyChain) {
        checkNotNull(dataModelPropertyChain);
        checkNotNull(propertyChain);

        if(dataModelPropertyChain.isEmpty()) {
            return false;
        }

        final Iterable<String> propertyNames = toPropertyNames(propertyChain);
        final LinkedList<DataModelProperty> appendedDataModelChain = new LinkedList<>();

        boolean aborted = false;
        DataModelProperty dataModelProperty = dataModelPropertyChain.getLast();
        try {

            for(final String propertyName : propertyNames) {

                final Object dataModel = dataModelProperty.getDataModel();
                final Class<?> dataModelClass = dataModel.getClass();
                final Optional<Method> dataModelGetter = getGetterMethod(dataModelClass,
                                                                         propertyName);

                if(dataModelGetter.isPresent()) {
                    final Object nextDataModel = dataModelGetter.get().invoke(dataModel);
                    if(nextDataModel == null) {
                        aborted = true;
                        break;
                    }

                    dataModelProperty = new DataModelPropertyImpl(nextDataModel,
                                                              propertyName);
                    appendedDataModelChain.add(dataModelProperty);
                } else {
                    aborted = true;
                    break;
                }
            }
        } catch(final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOG.error(String.format("Can not access getter on %s. Is it a no argument public method?",
                                    dataModelProperty),
                      e);
            aborted = true;
        }

        dataModelPropertyChain.addAll(appendedDataModelChain);
        return !aborted;
    }

    private static Iterable<String> toPropertyNames(final String subModelPath) {
        checkNotNull(subModelPath);

        return Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
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
