package org.trinity.foundation.api.binding.binding;

import java.util.Collection;

public interface ViewBinding {

    ViewBindingMeta getViewBindingMeta();

    Collection<DataModelProperty> bind();

    void unbind();
}
