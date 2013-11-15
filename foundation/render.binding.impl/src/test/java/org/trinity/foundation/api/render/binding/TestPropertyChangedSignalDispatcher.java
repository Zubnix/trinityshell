package org.trinity.foundation.api.render.binding;


import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Key.class)
public class TestPropertyChangedSignalDispatcher {
    @Mock
    private ViewBinder viewBinder;
    @Mock
    private Injector injector;
    @Mock
    private MethodInvocation methodInvocation;

    @InjectMocks
    private PropertyChangedSignalDispatcher propertyChangedSignalDispatcher;

    @Test
    public void testPropertiesUpdated() throws Throwable {
        //given
        //a method invocation signaling multiple properties that changed
        //a PropertyChangedSignalDispatcher
        final Object invocationResult = mock(Object.class);
        final Object changedModel = mock(Object.class);
        final Method method = PowerMockito.spy(getClass().getMethod("dummyMethod"));

        final ListeningExecutorService modelExecutorInstance = mock(ListeningExecutorService.class);
        final Key executorKey = mock(Key.class);

        final String prop0 = "prop0";
        final String prop1 = "prop1";
        final String prop2 = "prop2";

        when(methodInvocation.proceed()).thenReturn(invocationResult);
        when(methodInvocation.getThis()).thenReturn(changedModel);
        when(methodInvocation.getMethod()).thenReturn(method);

        mockStatic(Key.class);
        when(Key.get(ListeningExecutorService.class, Annotation.class)).thenReturn(executorKey);
        when(injector.getInstance(executorKey)).thenReturn(modelExecutorInstance);


        //when
        //the PropertyChangedSignalDispatcher is invoked
        final Object result = propertyChangedSignalDispatcher.invoke(methodInvocation);

        //then
        // the PropertyChangedSignalDispatcher executes the method
        //the viewbinder is requested to update the binding
        assertEquals(invocationResult,result);
        verify(viewBinder).updateDataModelBinding(modelExecutorInstance, changedModel, prop0);
        verify(viewBinder).updateDataModelBinding(modelExecutorInstance,changedModel,prop1);
        verify(viewBinder).updateDataModelBinding(modelExecutorInstance,changedModel,prop2);
    }

    @PropertyChanged(value = {"prop0","prop1","prop2"},executor = Annotation.class)
    public void dummyMethod(){
    }
}
