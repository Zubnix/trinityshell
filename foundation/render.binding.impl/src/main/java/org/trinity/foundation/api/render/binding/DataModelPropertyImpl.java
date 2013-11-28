package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

public class DataModelPropertyImpl implements DataModelProperty {

    private static final Logger LOG = LoggerFactory.getLogger(DataModelPropertyImpl.class);

    private final Object dataModel;
    private final String propertyName;

    public DataModelPropertyImpl(final Object dataModel,
                                 final String propertyName) {
        this.dataModel = dataModel;
        this.propertyName = propertyName;
    }

    @Override
    public Object getDataModel() {
        return this.dataModel;
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public Optional<Object> getPropertyValue() {
        final Optional<Method> getterMethod = DataContextNavigator.getGetterMethod(getDataModel().getClass(),
                                                                                   getPropertyName());
        final Optional<Object> propertyValue;
        if(getterMethod.isPresent()) {
            try {
                final Object property = getterMethod.get().invoke(getDataModel());
                propertyValue = Optional.fromNullable(property);
            } catch(IllegalAccessException | InvocationTargetException e) {
                LOG.error(MessageFormat.format("Unable to retrieve property {0} from {1}",
                                               getPropertyName(),
                                               getDataModel()),
                          e);
                propertyValue = Optional.absent();
            }
        } else {
            propertyValue = Optional.absent();
        }

        return propertyValue;
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final DataModelProperty other = (DataModelProperty) obj;

        return com.google.common.base.Objects.equal(getDataModel(),
                                                    other.getDataModel())
                && com.google.common.base.Objects.equal(getPropertyName(),
                                                        other.getPropertyName());
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(getDataModel(),
                                                       getPropertyName());
    }
}
