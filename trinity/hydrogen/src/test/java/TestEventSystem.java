import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Erik De Rijcke
 * 
 */
public class TestEventSystem {

	@Test
	public void testEventBusConcurrency() {
		// TODO
	}

	@Test
	public void testEventBusRecursion() {
		// TODO ?
	}

	@Test
	public void testEventBusAddListener() {
		final TestEventBus testEventBus = new TestEventBus();
		testEventBus.addEventHandler(new TestEventHandler(),
		                             TestEvent.TYPE);
		Assert.assertEquals(1,
		                    testEventBus.getEventHandlerSwitch().size());
		Assert.assertEquals(1,
		                    testEventBus.getEventHandlerSwitch()
		                                    .get(TestEvent.TYPE).size());

		testEventBus.addEventHandler(new TestEventHandler(),
		                             TestEvent.TYPE);
		Assert.assertEquals(1,
		                    testEventBus.getEventHandlerSwitch().size());
		Assert.assertEquals(2,
		                    testEventBus.getEventHandlerSwitch()
		                                    .get(TestEvent.TYPE).size());

	}

	@Test
	public void testEventBusRemoveListener() {
		final TestEventBus testEventBus = new TestEventBus();

		final TestEventHandler testEventHandler0 = new TestEventHandler();

		testEventBus.addEventHandler(testEventHandler0,
		                             TestEvent.TYPE);
		testEventBus.removeEventHandler(testEventHandler0,
		                                TestEvent.TYPE);

		Assert.assertEquals(0,
		                    testEventBus.getEventHandlerSwitch()
		                                    .get(TestEvent.TYPE).size());

		final TestEventHandler testEventHandler1 = new TestEventHandler();
		final TestEventHandler testEventHandler2 = new TestEventHandler();

		testEventBus.addEventHandler(testEventHandler1,
		                             TestEvent.TYPE);
		testEventBus.addEventHandler(testEventHandler2,
		                             TestEvent.TYPE);
		testEventBus.removeEventHandler(testEventHandler1,
		                                TestEvent.TYPE);
		Assert.assertEquals(1,
		                    testEventBus.getEventHandlerSwitch()
		                                    .get(TestEvent.TYPE).size());
	}
}
