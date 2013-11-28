package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public class RootDataModelProperty implements DataModelProperty {

    @Nonnull
    private final Object dataModel;
    private final Optional<Object> dataModelObjectOptional;
    private final String propertyName = "";

    public RootDataModelProperty(@Nonnull final Object dataModel) {
        this.dataModel = dataModel;
        this.dataModelObjectOptional = Optional.of(this.dataModel);
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
        return this.dataModelObjectOptional;
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
