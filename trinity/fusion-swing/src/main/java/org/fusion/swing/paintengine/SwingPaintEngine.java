package org.fusion.swing.paintengine;

import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.peer.ComponentPeer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.fusion.swing.painter.SwingFusionPainter.ReturnValueWrapper;
 
import org.hydrogen.error.HydrogenPaintInterfaceException;
import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.Paintable;

// TODO extract base paint engine also usable for use with qfusionrenderengine
public class SwingPaintEngine {

	private final Map<Paintable, Component> components;

	public SwingPaintEngine() {
		this.components = new HashMap<Paintable, Component>();
	}

	public void destroy(final Paintable paintable)
	                   {
		this.components.get(paintable).removeNotify();
		this.components.remove(paintable);
	}

	public void setInputFocus(final Paintable paintable)
	                   {
		// Swing fail:
		// Swing doesn't seem to enforce this on *nix.
		// TODO find a way to clear all other focus requests before requesting
		// the focus on this window.
		this.components.get(paintable).requestFocus();
	}

	public void lower(final Paintable paintable)
	                   {
		final Component component = this.components.get(paintable);
		if (component instanceof Window) {
			final Window componentFrame = (Window) component;
			componentFrame.toBack();
			componentFrame.repaint();
		}
	}

	public void show(final Paintable paintable)
	                   {
		this.components.get(paintable).setVisible(true);
	}

	public void move(final Paintable paintable,
	                 final int x,
	                 final int y)    {
		this.components.get(paintable).setLocation(x,
		                                           y);
	}

	public void moveResize(final Paintable paintable,
	                       final int x,
	                       final int y,
	                       final int width,
	                       final int height)
	                   {
		final Component component = this.components.get(paintable);
		component.setLocation(x,
		                      y);
		component.setSize(width,
		                  width);
	}

	public void raise(final Paintable paintable)
	                   {
		// Swing fail;
		// Swing doesn'seem to support this on non frame type.
		final Component component = this.components.get(paintable);
		if (component instanceof Window) {
			final Window componentFrame = (Window) component;
			componentFrame.toFront();
			componentFrame.repaint();
		} else {
			throw new Error("Swing only supports raising on an awt Window.");
		}
	}

	public void setParent(final Paintable paintable,
	                      final Paintable parent,
	                      final int x,
	                      final int y)    {
		// Swing fail:
		// Swing doesn't seem to support this on non container type.
		final Component parentComponent = this.components.get(parent);
		if (parentComponent instanceof Container) {
			final Container parentContainer = (Container) parentComponent;
			parentContainer.add(this.components.get(paintable));
			parentContainer.validate();
		} else {
			throw new Error("Swing only supports reparenting to an awt Container.");
		}
	}

	public void resize(final Paintable paintable,
	                   final int width,
	                   final int height)
	                   {
		this.components.get(paintable).setSize(width,
		                                       height);
	}

	public void hide(final Paintable paintable)
	                   {
		this.components.get(paintable).setVisible(false);
	}

	public void createRenderArea(final Paintable parentPaintable,
	                             final Paintable paintable,
	                             final PaintCall<Component, ? extends Component> paintCall,
	                             final ReturnValueWrapper<Long> returnValue)
	                throws HydrogenPaintInterfaceException {
		// Swing fail:
		// Swing can not/will not give a a root window frame..
		// TODO Implement a custom component that represents the root window.

		final Component parentComponent = this.components.get(parentPaintable);
		final Component newComponent = paintCall.call(parentComponent,
		                                              paintable);
		newComponent.addNotify();

		// Semi swing fail:
		// It's very cumbersome to retrieve a native window handle. All the
		// other window toolkits do this in a (near) one liner.
		@SuppressWarnings("deprecation")
		final ComponentPeer componentPeer = newComponent.getPeer();
		Class<?> classToFind = componentPeer.getClass();
		while (!classToFind.getName().equals("XBaseWindow")) {
			classToFind = classToFind.getSuperclass();
		}
		Field windowField;
		try {
			windowField = classToFind.getDeclaredField("window");
		} catch (final SecurityException e) {
			throw new HydrogenPaintInterfaceException(e);
		} catch (final NoSuchFieldException e) {
			throw new HydrogenPaintInterfaceException(e);
		}

		long windowId;
		try {
			windowId = windowField.getLong(componentPeer);
		} catch (final IllegalArgumentException e) {
			throw new HydrogenPaintInterfaceException(e);
		} catch (final IllegalAccessException e) {
			throw new HydrogenPaintInterfaceException(e);
		}

		returnValue.setReturnValue(Long.valueOf(windowId));
	}

	public void paint(final Paintable paintable,
	                  final PaintCall<Component, ? extends Component> paintCall)
	                throws HydrogenPaintInterfaceException {
		final Component component = paintCall
		                .call(this.components.get(paintable),
		                      paintable);
		if (component != null) {
			this.components.put(paintable,
			                    component);
		}
	}

	public void grabKeyboard(final Paintable paintable)
	                throws HydrogenPaintInterfaceException {
		// Swing fail?:
		// TODO How to tell swing to grab the keyboard and redirect all keyinput
		// to this widget? For now we kindly request to pretty please give us
		// the focus. pweeeease?? :'(
		this.components.get(paintable).requestFocus();
	}

	public void releaseKeyboard(final Paintable paintable)
	                throws HydrogenPaintInterfaceException {
		// Swing fail? (see grabkeyboard)
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		                .clearGlobalFocusOwner();
	}

	public void grabMouse(final Paintable paintable)
	                throws HydrogenPaintInterfaceException {
		// Swing fail?:
		// TODO How to tell swing to grab the mouse and redirect all buttoninput
		// to this widget? For now we kindly request to pretty please give us
		// the focus. pweeeease?? :'(
		this.components.get(paintable).requestFocus();
	}

	public void releaseMouse(final Paintable paintable)
	                throws HydrogenPaintInterfaceException {
		// Swing fail? (see grabmouse)
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		                .clearGlobalFocusOwner();
	}
}
