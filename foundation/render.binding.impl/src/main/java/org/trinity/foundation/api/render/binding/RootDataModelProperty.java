package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public class RootDataModelProperty implements DataModelProperty {

    private final Optional<Object> dataModelObjectOptional;

    public RootDataModelProperty(@Nonnull final Object rootDataModel) {
        this.dataModelObjectOptional = Optional.of(rootDataModel);
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
        final RootDataModelProperty other = (RootDataModelProperty) obj;

        return com.google.common.base.Objects.equal(this.dataModelObjectOptional,
                                                    other.dataModelObjectOptional);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(this.dataModelObjectOptional);
    }
}
