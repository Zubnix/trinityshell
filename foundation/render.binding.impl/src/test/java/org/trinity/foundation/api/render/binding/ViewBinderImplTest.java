package org.trinity.foundation.api.render.binding;


import com.google.inject.Injector;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocationDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;

public class ViewBinderImplTest {
    @Mock
    private Injector injector;
    @Mock
    private PropertySlotInvocationDelegate propertySlotInvocationDelegate;
    @Mock
    private SubViewModelDelegate subViewModelDelegate;
    @InjectMocks
    private ViewBinderImpl viewBinder;

    //view events
    private void testBindViewEvents(){
        //given
        //a datamodel
        //a viewmodel with view events
        //a viewbinder

        //when
        //the viewmodel is bound to the datamodel
        //the viewmodel emits a view event

        //then
        //the datamodel is notified of the view event

    }

    private void testBindViewEventsWithDataContext(){
        //given
        //a datamodel
        //a viewmodel with view events with a data context
        //a viewbinder

        //when
        //the viewmodel is bound to the datamodel
        //the viewmodel emits a view event

        //then
        //the datacontext of the datamodel is notified of the view event

    }

    private void testSubviewsBindViewEvents(){
        //given
        //a datamodel
        //a viewmodel with a subview model with view events
        //a viewbinder

        //when
        //the viewmodel is bound to the datamodel
        //the subview model emits a view event

        //then
        //the datamodel is notified of the subview event
    }

    private void testSubviewsBindViewEventsWithDataContext(){
        //given
        //a datamodel
        //a viewmodel with a subview model with view events with a data context
        //a viewbinder

        //when
        //the viewmodel is bound to the datamodel
        //the subview model emits a view event

        //then
        //the datacontext of datamodel is notified of the subview event
    }

    private void testUpdateSubviewsBindViewEvents(){
        //given
        //a datamodel
        //a viewmodel with a subview model with view events
        //a new subview model
        //a viewbinder

        //when
        //the viewmodel is bound to the datamodel
        //the subview is updated with the new subviewmodel
        //the new subview emits a view event

        //then
        //the datamodel is notified of the subview event
    }

    private void testUpdateSubviewsBindViewEventsWithDataContext(){
        //given
        //a datamodel
        //a viewmodel with a subview model with view events with a data context
        //a new subview model
        //a viewbinder

        //when
        //the viewmodel is bound to the datamodel
        //the subview is updated with the new subviewmodel
        //the new subview emits a view event

        //then
        //the datacontext of the datamodel is notified of the subview event
    }


    //observable collections
    private void testBindObservableCollections(){

    }

    private void testSubviewsBindObservableCollections(){

    }

    private void testUpdateSubviewsBindObservableCollections(){

    }


    //model properties
    private void testBindProperties(){

    }

    private void testSubviewsBindProperties(){

    }

    private void testUpdateSubviewsBindProperties(){

    }
}
