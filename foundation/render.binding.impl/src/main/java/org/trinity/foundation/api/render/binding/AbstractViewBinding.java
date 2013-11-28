package org.trinity.foundation.api.render.binding;

import java.util.Collection;

public abstract class AbstractViewBinding {
    private final ViewBindingMeta viewBindingMeta;

    public AbstractViewBinding(final ViewBindingMeta viewBindingMeta) {
        this.viewBindingMeta = viewBindingMeta;
    }

    public ViewBindingMeta getViewBindingMeta() {
        return this.viewBindingMeta;
    }

    public abstract Collection<DataModelProperty> bind();

    public abstract void unbind();
}
