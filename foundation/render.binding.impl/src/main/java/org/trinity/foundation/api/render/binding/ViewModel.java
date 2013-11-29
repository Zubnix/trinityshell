package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;

/**
 *
 */
public class ViewModel {

    private final Object parentViewModel;
    private final String viewName;

    public ViewModel(final Object parentViewModel, final String viewName) {
        this.parentViewModel = parentViewModel;
        this.viewName = viewName;
    }

    public Object getParentViewModel() {
        return parentViewModel;
    }

    public String getViewName() {
        return viewName;
    }

    public Optional<Object> getViewModelValue(){

    }
}
