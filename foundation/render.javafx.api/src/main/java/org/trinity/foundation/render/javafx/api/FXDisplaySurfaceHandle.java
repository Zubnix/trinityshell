package org.trinity.foundation.render.javafx.api;

import com.sun.javafx.tk.quantum.WindowStage;
import javafx.scene.Node;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;

import java.lang.reflect.Field;

public class FXDisplaySurfaceHandle implements DisplaySurfaceHandle {

    private static final Logger LOG = LoggerFactory.getLogger(FXDisplaySurfaceHandle.class);

    private final Node node;

    public FXDisplaySurfaceHandle(Node node) {
        this.node = node;
    }

    @Override
    public Long getNativeHandle() {
        final Window window = node.getScene().getWindow();
        WindowStage stagePeer = (WindowStage) window.impl_getPeer();
        try {
            final Field platformWindowField = WindowStage.class.getDeclaredField("platformWindow");
            platformWindowField.setAccessible(true);
            com.sun.glass.ui.Window platformWindow = (com.sun.glass.ui.Window) platformWindowField.get(stagePeer);
            return platformWindow.getNativeWindow();
        } catch(NoSuchFieldException | IllegalAccessException e) {
            LOG.error("Error while trying to find native window id from JavaFX Node " + node,
                      e);
        }
        return 0L;
    }

    @Override
    public int hashCode() {
        return getNativeHandle().intValue();
    }

    @Override
    public boolean equals(final Object obj) {
        if(!(obj instanceof DisplaySurfaceHandle)) {
            return false;
        }

        DisplaySurfaceHandle otherObj = (DisplaySurfaceHandle) obj;

        return getNativeHandle().equals(otherObj.getNativeHandle());
    }
}
