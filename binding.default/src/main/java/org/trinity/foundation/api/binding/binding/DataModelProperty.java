package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public interface DataModelProperty {

    @Nonnull
    Optional<Object> getPropertyValue();
}
