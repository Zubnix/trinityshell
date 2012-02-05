import org.hydrogen.eventsystem.Event;

public class TestEvent implements Event<TestEventType> {

	public static final TestEventType TYPE = new TestEventType();

	@Override
	public TestEventType getType() {
		return TestEvent.TYPE;
	}

}
