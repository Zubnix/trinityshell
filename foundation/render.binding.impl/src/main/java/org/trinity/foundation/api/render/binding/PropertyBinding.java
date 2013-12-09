package org.trinity.foundation.api.render.binding;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.Collection;

public class PropertyBinding extends AbstractViewBinding{

    @Inject
    PropertyBinding(@Assisted final ViewBindingMeta viewBindingMeta) {
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
