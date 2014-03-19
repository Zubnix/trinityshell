package org.trinity.foundation.api.binding.binding;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface ViewBinding {

    @Nonnull
    ViewBindingMeta getViewBindingMeta();

    @Nonnull
    Collection<DataModelProperty> bind();

    void unbind();
}
