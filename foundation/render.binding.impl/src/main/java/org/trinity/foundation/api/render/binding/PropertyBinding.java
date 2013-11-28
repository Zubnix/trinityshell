package org.trinity.foundation.api.render.binding;

import java.util.Collection;

public class PropertyBinding extends AbstractViewBinding{
    public PropertyBinding(final ViewBindingMeta viewBindingMeta) {
        super(viewBindingMeta);
    }

    @Override
    public Collection<DataModelProperty> bind() {
        return null;
    }

    @Override
    public void unbind() {

    }
}
