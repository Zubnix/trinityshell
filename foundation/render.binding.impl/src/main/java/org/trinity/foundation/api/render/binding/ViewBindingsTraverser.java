package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import com.google.common.collect.TreeTraverser;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ViewBindingsTraverser extends TreeTraverser<ViewBindingMeta> {

    @Override
    public Iterable<ViewBindingMeta> children(final ViewBindingMeta root) {
        final Object viewModel = root.getViewModel();
        final Class<?> viewModelClass = viewModel.getClass();
		final Field[] declaredFields = ViewBindingsUtil.getFields(viewModelClass);

		final List<ViewBindingMeta> viewBindingMetas = new LinkedList<>();
		for(final Field declaredField : declaredFields) {

			try {
				final Object subViewValue = declaredField.get(viewModel);
				if(subViewValue == null) {
					continue;
				}
				final Optional<ViewBindingMeta> viewBindingMetaOptional = ViewBindingMeta.create(root,
																								 declaredField,
																								 subViewValue);
				if(viewBindingMetaOptional.isPresent()) {
					viewBindingMetas.add(viewBindingMetaOptional.get());
				}
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return viewBindingMetas;
    }
}
