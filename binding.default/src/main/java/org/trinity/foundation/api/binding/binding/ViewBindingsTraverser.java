package org.trinity.foundation.api.binding.binding;

import com.google.common.collect.Maps;
import com.google.common.collect.TreeTraverser;
import org.trinity.binding.api.view.SubView;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewBindingsTraverser extends TreeTraverser<ViewBindingMeta> {

    private static final Map<Class<?>, Field[]> FIELDS_CACHE = Maps.newHashMap();

    @Inject
    ViewBindingsTraverser() {
    }

    @Override
    public Iterable<ViewBindingMeta> children(@Nonnull final ViewBindingMeta root) {
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

				final SubView subView = declaredField.getAnnotation(SubView.class);
				if(subView == null) {
					continue;
				}

				final ViewBindingMeta viewBindingMeta = SubViewBindingMeta.create(root,
																			   declaredField,
																			   subViewValue);
				viewBindingMetas.add(viewBindingMeta);
			}
			catch(final IllegalAccessException e) {
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
