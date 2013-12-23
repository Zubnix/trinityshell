package org.trinity.shellplugin.wm.view.javafx;

import com.google.common.base.Objects;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;

import javax.annotation.Nonnull;

import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

@Bind(to=@To(IMPLEMENTATION))
public class PointerInputFilter implements EventSignalFilter {

    public static class MouseEventHandler implements EventHandler<MouseEvent>{

        @Nonnull
        private final Signal signal;

        public MouseEventHandler(@Nonnull final Signal signal) {
            this.signal = signal;
        }

        @Override
        public void handle(final MouseEvent mouseEvent) {
            this.signal.fire();
        }

        //we override the equals & hashcode so that eventhandlers with the same signal
        //are considered to be the same.

        @Override
        public boolean equals(final Object obj) {
            if(obj == null) {
                return false;
            }
            if(getClass() != obj.getClass()) {
                return false;
            }

            final MouseEventHandler other = (MouseEventHandler) obj;
            return Objects.equal(this.signal,
                                 other.signal);
        }

        @Override
        public int hashCode() {
            return this.signal.hashCode();
        }
    }

    PointerInputFilter() {
    }

    @Override
    public void installFilter(@Nonnull final Object view,
                              @Nonnull final Signal signal) {

        final EventHandler<MouseEvent> eventHandler = new MouseEventHandler(signal);
        final Node node = (Node) view;
        node.addEventHandler(MOUSE_CLICKED,
                             eventHandler);
    }

    @Override
    public void uninstallFilter(@Nonnull final Object view,
                                @Nonnull final Signal signal) {

        final EventHandler<MouseEvent> eventHandler = new MouseEventHandler(signal);
        final Node node = (Node) view;
        node.removeEventHandler(MOUSE_CLICKED,
                                eventHandler);
    }
}