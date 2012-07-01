package org.trinity.display.x11.impl.xcb;

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCallExceptionHandler;
import org.trinity.display.x11.core.api.XEventBridge;
import org.trinity.display.x11.core.api.XEventConverter;
import org.trinity.display.x11.impl.xcb.connectioncall.CloseConnection;
import org.trinity.display.x11.impl.xcb.connectioncall.OpenConnection;
import org.trinity.display.x11.impl.xcb.displaycall.GetInputFocus;
import org.trinity.display.x11.impl.xcb.displaycall.GetNextEvent;
import org.trinity.display.x11.impl.xcb.error.Xcb4jExceptionHandler;
import org.trinity.display.x11.impl.xcb.eventconverters.XButtonPressedEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XButtonReleaseEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XClientMessageEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XConfigureEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XConfigureRequestEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XDestroyWindowEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XFocusInEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XFocusOutEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XKeyPressedEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XKeyReleasedEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XMapEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XMapRequestEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XMouseEnterEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XMouseLeaveEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XPropertyEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XSelectionClearEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XSelectionEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XSelectionRequestEventConverter;
import org.trinity.display.x11.impl.xcb.eventconverters.XUnmapEventConverter;
import org.trinity.display.x11.impl.xcb.windowcall.AddToSaveSet;
import org.trinity.display.x11.impl.xcb.windowcall.DestroyWindow;
import org.trinity.display.x11.impl.xcb.windowcall.FocusWindow;
import org.trinity.display.x11.impl.xcb.windowcall.GetWindowAttributes;
import org.trinity.display.x11.impl.xcb.windowcall.GetWindowGeometry;
import org.trinity.display.x11.impl.xcb.windowcall.GrabButton;
import org.trinity.display.x11.impl.xcb.windowcall.GrabKey;
import org.trinity.display.x11.impl.xcb.windowcall.GrabKeyboard;
import org.trinity.display.x11.impl.xcb.windowcall.GrabMouse;
import org.trinity.display.x11.impl.xcb.windowcall.LowerWindow;
import org.trinity.display.x11.impl.xcb.windowcall.MapWindow;
import org.trinity.display.x11.impl.xcb.windowcall.MoveResizeWindow;
import org.trinity.display.x11.impl.xcb.windowcall.MoveWindow;
import org.trinity.display.x11.impl.xcb.windowcall.RaiseWindow;
import org.trinity.display.x11.impl.xcb.windowcall.RemoveFromSaveSet;
import org.trinity.display.x11.impl.xcb.windowcall.ReparentWindow;
import org.trinity.display.x11.impl.xcb.windowcall.ResizeWindow;
import org.trinity.display.x11.impl.xcb.windowcall.SelectEvents;
import org.trinity.display.x11.impl.xcb.windowcall.SendClientMessage;
import org.trinity.display.x11.impl.xcb.windowcall.TranslateCoordinates;
import org.trinity.display.x11.impl.xcb.windowcall.UngrabButton;
import org.trinity.display.x11.impl.xcb.windowcall.UngrabKey;
import org.trinity.display.x11.impl.xcb.windowcall.UngrabKeyboard;
import org.trinity.display.x11.impl.xcb.windowcall.UngrabMouse;
import org.trinity.display.x11.impl.xcb.windowcall.UnmapWindow;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

public class Xcb4jModule extends AbstractModule {

	@Override
	protected void configure() {
		configureXcb4J();
		configureXcbConnectionCalls();
		configureXcbDisplayServerCalls();
		configureXcbErrors();
		configureXcbEventConverters();
		configureXcbWindowCalls();
	}

	private void configureXcb4J() {
		bind(XEventBridge.class).to(XEventBridgeImpl.class);
		bind(Runnable.class).annotatedWith(Names.named("XEventPump"))
				.to(XEventPump.class);
	}

	private void configureXcbWindowCalls() {
		bind(XCall.class).annotatedWith(Names.named("AddToSaveSet"))
				.to(AddToSaveSet.class);
		bind(XCall.class).annotatedWith(Names.named("DestroyWindow"))
				.to(DestroyWindow.class);
		bind(XCall.class).annotatedWith(Names.named("FocusWindow"))
				.to(FocusWindow.class);
		bind(XCall.class).annotatedWith(Names.named("GetWindowAttributes"))
				.to(GetWindowAttributes.class);
		bind(XCall.class).annotatedWith(Names.named("GetWindowGeometry"))
				.to(GetWindowGeometry.class);
		bind(XCall.class).annotatedWith(Names.named("GrabButton"))
				.to(GrabButton.class);
		bind(XCall.class).annotatedWith(Names.named("GrabKey"))
				.to(GrabKey.class);
		bind(XCall.class).annotatedWith(Names.named("GrabKeyboard"))
				.to(GrabKeyboard.class);
		bind(XCall.class).annotatedWith(Names.named("GrabMouse"))
				.to(GrabMouse.class);
		bind(XCall.class).annotatedWith(Names.named("LowerWindow"))
				.to(LowerWindow.class);
		bind(XCall.class).annotatedWith(Names.named("MapWindow"))
				.to(MapWindow.class);
		bind(XCall.class).annotatedWith(Names.named("MoveResizeWindow"))
				.to(MoveResizeWindow.class);
		bind(XCall.class).annotatedWith(Names.named("MoveWindow"))
				.to(MoveWindow.class);
		bind(XCall.class).annotatedWith(Names.named("RaiseWindow"))
				.to(RaiseWindow.class);
		bind(XCall.class).annotatedWith(Names.named("RemoveFromSaveSet"))
				.to(RemoveFromSaveSet.class);
		bind(XCall.class).annotatedWith(Names.named("ReparentWindow"))
				.to(ReparentWindow.class);
		bind(XCall.class).annotatedWith(Names.named("ResizeWindow"))
				.to(ResizeWindow.class);
		bind(XCall.class).annotatedWith(Names.named("SelectEvents"))
				.to(SelectEvents.class);
		bind(XCall.class).annotatedWith(Names.named("SendClientMessage"))
				.to(SendClientMessage.class);
		bind(XCall.class).annotatedWith(Names.named("TranslateCoordinates"))
				.to(TranslateCoordinates.class);
		bind(XCall.class).annotatedWith(Names.named("UngrabButton"))
				.to(UngrabButton.class);
		bind(XCall.class).annotatedWith(Names.named("UngrabKey"))
				.to(UngrabKey.class);
		bind(XCall.class).annotatedWith(Names.named("UngrabKeyboard"))
				.to(UngrabKeyboard.class);
		bind(XCall.class).annotatedWith(Names.named("UngrabMouse"))
				.to(UngrabMouse.class);
		bind(XCall.class).annotatedWith(Names.named("UnmapWindow"))
				.to(UnmapWindow.class);
	}

	private void configureXcbEventConverters() {
		@SuppressWarnings("rawtypes")
		final Multibinder<XEventConverter> eventConvertMultibinder = Multibinder
				.newSetBinder(	binder(),
								XEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XButtonPressedEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XButtonReleaseEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XClientMessageEventConverter.class);
		eventConvertMultibinder.addBinding().to(XConfigureEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XConfigureRequestEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XDestroyWindowEventConverter.class);
		eventConvertMultibinder.addBinding().to(XFocusInEventConverter.class);
		eventConvertMultibinder.addBinding().to(XFocusOutEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XKeyPressedEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XKeyReleasedEventConverter.class);
		eventConvertMultibinder.addBinding().to(XMapEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XMapRequestEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XMouseEnterEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XMouseLeaveEventConverter.class);
		eventConvertMultibinder.addBinding().to(XPropertyEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XSelectionClearEventConverter.class);
		eventConvertMultibinder.addBinding().to(XSelectionEventConverter.class);
		eventConvertMultibinder.addBinding()
				.to(XSelectionRequestEventConverter.class);
		eventConvertMultibinder.addBinding().to(XUnmapEventConverter.class);
	}

	private void configureXcbErrors() {
		bind(XCallExceptionHandler.class).to(Xcb4jExceptionHandler.class);
	}

	private void configureXcbDisplayServerCalls() {
		bind(XCall.class).annotatedWith(Names.named("GetInputFocus"))
				.to(GetInputFocus.class);
		bind(XCall.class).annotatedWith(Names.named("GetNextEvent"))
				.to(GetNextEvent.class);

	}

	private void configureXcbConnectionCalls() {
		bind(XCall.class).annotatedWith(Names.named("CloseConnection"))
				.to(CloseConnection.class);
		bind(XCall.class).annotatedWith(Names.named("OpenConnection"))
				.to(OpenConnection.class);

	}
}