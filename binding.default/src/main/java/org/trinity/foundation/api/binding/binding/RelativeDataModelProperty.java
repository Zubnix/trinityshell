package org.trinity.foundation.api.binding.binding;

import com.google.auto.value.AutoValue;
import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

@Immutable
@AutoValue
public abstract class RelativeDataModelProperty implements DataModelProperty {

    private static final Logger LOG = LoggerFactory.getLogger(RelativeDataModelProperty.class);

    private static final Table<Class<?>, String, Optional<Method>> GETTER_CACHE = HashBasedTable.create();
    private static final String GET_BOOLEAN_PREFIX = "is";
    private static final String GET_PREFIX         = "get";

    public static DataModelProperty create( @Nonnull final Object dataModel,
                                            @Nonnull final String propertyName){
        return new AutoValue_RelativeDataModelProperty(dataModel, propertyName);
    }

    abstract Object getDataModel();

    abstract String getPropertyName();

    @Override
    public Optional<Object> getPropertyValue() {
		final Optional<Method> getterMethod = getGetterMethod(getDataModel().getClass(),
																			   getPropertyName());
		Optional<Object> propertyValue = Optional.absent();
        if(getterMethod.isPresent()) {
            try {
                final Object property = getterMethod.get().invoke(getDataModel());
                propertyValue = Optional.fromNullable(property);
            } catch(IllegalAccessException | InvocationTargetException e) {
                LOG.error(MessageFormat.format("Unable to retrieve property {0} from {1}",
                                               getPropertyName(),
                        getDataModel()),
                                                e);
            }
        }

        return propertyValue;
    }

    public Optional<Method> getGetterMethod(final Class<?> dataModelClass,
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

    private String toGetterMethodName(final String propertyName) {
        return toGetterMethodName(GET_PREFIX,
                propertyName);
    }

    private String toGetterMethodName(final String prefix,
                                             final String propertyName) {
        checkNotNull(prefix);
        checkNotNull(propertyName);

        return prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,
                propertyName);
    }

    private String toBooleanGetterMethodName(final String propertyName) {
        return toGetterMethodName(GET_BOOLEAN_PREFIX,
                propertyName);
    }
}
