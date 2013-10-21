package org.trinity.shellplugin.wm.view.javafx.skin;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;

import javax.annotation.Nonnull;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;

public class PointerInputFilter implements EventSignalFilter {
    @Override
    public void installFilter(@Nonnull final Object view,
                              @Nonnull final Signal signal) {
        Node node = (Node) view;
        node.addEventHandler(MOUSE_CLICKED,
                             new EventHandler<MouseEvent>() {
                                 public void handle(MouseEvent event) {
                                     signal.fire();
                                 }
                             });
    }
}