package org.trinity.foundation.api.render.binding;

import com.google.common.util.concurrent.ListeningExecutorService;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;

/**
 *
 */
public interface CollectionBindingFactory {

    CollectionBinding create(ViewBindingMeta viewBindingMeta,
                             ObservableCollection observableCollection);
}
