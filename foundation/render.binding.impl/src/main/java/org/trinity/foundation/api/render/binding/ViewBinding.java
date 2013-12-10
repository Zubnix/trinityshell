package org.trinity.foundation.api.render.binding;

import java.util.Collection;

public interface ViewBinding {

    ViewBindingMeta getViewBindingMeta();

    Collection<DataModelProperty> bind();

    void unbind();
}
