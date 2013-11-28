package org.trinity.foundation.api.render.binding;

import java.util.Collection;

public class EventBinding extends AbstractViewBinding {
    public EventBinding(final ViewBindingMeta viewBindingMeta) {
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
