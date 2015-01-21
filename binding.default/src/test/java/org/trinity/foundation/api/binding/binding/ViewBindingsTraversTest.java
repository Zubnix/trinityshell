package org.trinity.foundation.api.binding.binding;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.binding.api.view.SubView;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ViewBindingsTraversTest {

    public static class ViewModel {

        @SubView
        private final NestedViewModel nestedViewModel = new NestedViewModel();

        private Object notASubView;
    }

    public static class NestedViewModel {

        @SubView
        private final DeepNestedViewModel deepNestedViewModel = new DeepNestedViewModel();
    }

    public static class DeepNestedViewModel {

        @SubView
        private Object absentSubView;
    }

    @Mock
    private ViewBindingMeta rootViewBindingMeta;

    @Before
    public void setUp() {
        final ViewModel viewModel = new ViewModel();
        when(this.rootViewBindingMeta.getViewModel()).thenReturn(viewModel);
    }

    @Test
    public void testSubViewTraversing() {
        //given
        //a view bindings traverser
        //a view model with multiple deep nested sub views
        final ViewBindingsTraverser viewBindingsTraverser = new ViewBindingsTraverser();

        //when
        //the view model is traversed
        final FluentIterable<ViewBindingMeta> viewBindingMetas = viewBindingsTraverser.preOrderTraversal(this.rootViewBindingMeta);
        final ImmutableList<ViewBindingMeta> viewBindingMetasList = viewBindingMetas.toList();

        //then
        //a collection of view binding metas is returned
        assertEquals(3,
                     viewBindingMetas.size());
        assertEquals(ViewModel.class,
                     viewBindingMetasList.get(0)
                                         .getViewModel()
                                         .getClass());
        assertEquals(NestedViewModel.class,
                     viewBindingMetasList.get(1)
                                         .getViewModel()
                                         .getClass());
        assertEquals(DeepNestedViewModel.class,
                     viewBindingMetasList.get(2)
                                         .getViewModel()
                                         .getClass());
    }
}
