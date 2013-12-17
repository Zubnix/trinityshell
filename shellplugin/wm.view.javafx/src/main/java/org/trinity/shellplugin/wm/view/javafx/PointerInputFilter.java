package org.trinity.shellplugin.wm.view.javafx;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;

import javax.annotation.Nonnull;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class PointerInputFilter implements EventSignalFilter {

    private final Table<Object,Signal,EventHandler<MouseEvent>> eventHandlerTable = HashBasedTable.create();

    @Override
    public void installFilter(@Nonnull final Object view,
                              @Nonnull final Signal signal) {

        final EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent event) {
                signal.fire();
            }
        };
        this.eventHandlerTable.put(view,signal,eventHandler);

        final Node node = (Node) view;
        node.addEventHandler(MOUSE_CLICKED,
                             eventHandler);
    }

    @Override
    public void uninstallFilter(@Nonnull final Object view,
                                @Nonnull final Signal signal) {

        final EventHandler<MouseEvent> eventHandler = this.eventHandlerTable.get(view, signal);

        final Node node = (Node) view;
        node.removeEventHandler(MOUSE_CLICKED,
                                eventHandler);
    }
}