package org.trinity.foundation.api.render.binding;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.Collection;

public class EventBinding extends AbstractViewBinding {

    @Inject
    EventBinding(@Assisted final ViewBindingMeta viewBindingMeta) {
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
