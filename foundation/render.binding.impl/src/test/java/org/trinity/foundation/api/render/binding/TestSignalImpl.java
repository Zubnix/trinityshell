package org.trinity.foundation.api.render.binding;


import com.google.common.util.concurrent.ListeningExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
public class TestSignalImpl {

    @Mock
    private ListeningExecutorService modelExecutor;
    @Mock
    private Object viewModel;
    @Mock
    private Map<Object, Object> dataModelByViewModel;
    @Mock
    private String inputSlotName;

    private final String targetMethodName = "targetMethod";


    @Test
    public void testFire() {
        //given
        //a SignalImpl
        when(modelExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object arg0 = invocation.getArguments()[0];
                final Callable<?> submittedCallable = (Callable<?>) arg0;
                final Object result = submittedCallable.call();
                return immediateFuture(result);
            }
        });

        final DummyDataModel dataModel = spy(new DummyDataModel());
        when(dataModelByViewModel.get(viewModel)).thenReturn(dataModel);

        final SignalImpl signal = new SignalImpl(modelExecutor,viewModel,dataModelByViewModel,targetMethodName);

        //when
        //a viewModel signal is fired
        signal.fire();

        //then
        //the bound model method is invoked.
        verify(dataModel).targetMethod();
    }

    public static class DummyDataModel {
        public void targetMethod() {

        }
    }
}
