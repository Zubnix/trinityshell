package org.trinity.shellplugin.widget.api.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.display.api.event.InputNotifyEvent;
import org.trinity.shell.api.widget.ShellWidget;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;

/***************************************
 * Input listener for a {@link ShellWidget}. It listens for an
 * {@link InputNotifyEvent} and invokes the corresponding method of the
 * {@code ShellWidget} that was annotated with {@link InputSlot}.
 * 
 *************************************** 
 */
public class InputSignalDispatcher {

	private final BindingDiscovery bindingDiscovery;
	private final ShellWidget shellWidget;

	/***************************************
	 * Create a new {@code InputSignalDispatcher} and attach it to the given
	 * {@link ShellWidget}.
	 * 
	 * @param shellWidget
	 * @param bindingDiscovery
	 *************************************** 
	 */
	public InputSignalDispatcher(ShellWidget shellWidget, BindingDiscovery bindingDiscovery) {
		this.bindingDiscovery = bindingDiscovery;
		this.shellWidget = shellWidget;
		this.shellWidget.addShellNodeEventHandler(this);
	}

	@Subscribe
	public void handleBoundButtonInputEvent(BoundInputEvent boundInputEvent) {
		try {
			Optional<Method> inputSlot = bindingDiscovery.lookupInputSlot(	shellWidget.getClass(),
																			boundInputEvent.getInput().getClass(),
																			boundInputEvent.getInputSlotName());
			if (inputSlot.isPresent()) {
				inputSlot.get().invoke(	shellWidget,
										boundInputEvent);
			}
		} catch (ExecutionException e) {
			Throwables.propagate(e);
		} catch (IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (InvocationTargetException e) {
			Throwables.propagate(e);
		}
	}

	/***************************************
	 * Release this {@code InputSignalDispatcher} from the {@link ShellWidget}
	 * that it was constructed with.
	 *************************************** 
	 */
	public void release() {
		this.shellWidget.removeShellNodeEventHandler(this);
	}
}