package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;

public interface DataModelProperty {
    Object getDataModel();

    String getPropertyName();

    Optional<Object> getPropertyValue();
}
