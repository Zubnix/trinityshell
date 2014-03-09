package org.trinity.foundation.api.binding.binding;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
@AutoValue
public abstract class ConstantDataModelProperty implements DataModelProperty {

    public static DataModelProperty create(@Nonnull final Object dataModel){
        return new AutoValue_ConstantDataModelProperty(Optional.of(dataModel));
    }
}