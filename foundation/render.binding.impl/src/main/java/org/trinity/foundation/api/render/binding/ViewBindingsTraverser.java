package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.TreeTraverser;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewBindingsTraverser extends TreeTraverser<ViewBindingMeta> {

    private static final Map<Class<?>, Field[]> FIELDS_CACHE = Maps.newHashMap();

    @Override
    public Iterable<ViewBindingMeta> children(final ViewBindingMeta root) {
        final Object viewModel = root.getViewModel();
        final Class<?> viewModelClass = viewModel.getClass();
		final Field[] declaredFields = getFields(viewModelClass);

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

    private Field[] getFields(final Class<?> viewModelClass) {
        Field[] fields = FIELDS_CACHE.get(viewModelClass);
        if(fields == null) {
            fields = viewModelClass.getDeclaredFields();
            for(final Field field : fields) {
                field.setAccessible(true);
            }
            FIELDS_CACHE.put(viewModelClass,
                    fields);
        }

        return fields;
    }
}
