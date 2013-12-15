package org.trinity.foundation.api.render.binding;

import com.google.common.util.concurrent.ListeningExecutorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SignalImplTest {

	public static class DataModel {
		public void onFoo() {

		}
	}

	@Mock
	private ListeningExecutorService dataModelExecutor;
	@Spy
	private DataModel                eventSignalReceiver;

	private SignalImpl signal;

	@Before
	public void setUp() {
		when(this.dataModelExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final Callable<?> submittedCallable = (Callable<?>) arg0;
				final Object result = submittedCallable.call();
				return immediateFuture(result);
			}
		});

	}

	@Test
	public void testFire() {
		//given
		//a signal with a valid slot
		this.signal = new SignalImpl(this.dataModelExecutor,
									 this.eventSignalReceiver,
									 "onFoo");

		//when
		//the signal is fired
		this.signal.fire();

		//then
		//the data model slot is invoked
		verify(this.eventSignalReceiver).onFoo();
	}

	@Test
	public void testFireNoSlot() {
		//given
		//a signal with an invalid slot
		this.signal = new SignalImpl(this.dataModelExecutor,
									 this.eventSignalReceiver,
									 "onBar");

		//when
		//the signal is fired
		this.signal.fire();

		//then
		//the data model slot is not invoked
		verify(this.eventSignalReceiver,
			   never()).onFoo();
	}
}
