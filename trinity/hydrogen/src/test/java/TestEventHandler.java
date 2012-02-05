import junit.framework.Assert;

import org.hydrogen.eventsystem.EventHandler;

public class TestEventHandler implements EventHandler<TestEvent> {
	@Override
	public void handleEvent(final TestEvent event)  {
		Assert.assertNotNull(event);
	}
}
