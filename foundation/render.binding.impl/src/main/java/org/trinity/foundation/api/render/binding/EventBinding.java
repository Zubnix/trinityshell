package org.trinity.foundation.api.render.binding;

import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import java.util.Collection;

public class EventBinding implements ViewBinding {
	private final ViewBindingMeta viewBindingMeta;

	@Inject
	EventBinding(@Assisted final ViewBindingMeta viewBindingMeta) {
		this.viewBindingMeta = viewBindingMeta;
	}

	@Override
	public ViewBindingMeta getViewBindingMeta() {
		return this.viewBindingMeta;
	}

	@Override
    public Collection<DataModelProperty> bind() {
        return null;
    }

    @Override
    public void unbind() {

    }
}
