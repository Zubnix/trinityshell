package org.trinity.foundation.render.javafx.api;


import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.TKStage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FXDisplaySurfaceHandle implements DisplaySurfaceHandle {

    private static final Logger LOG = LoggerFactory.getLogger(FXDisplaySurfaceHandle.class);

    private final Node node;

    public FXDisplaySurfaceHandle(final Node node) {
        this.node = node;
    }

    @Override
    public Long getNativeHandle() {
        Scene scene = this.node.getScene();
        if(scene == null) {
            return 0L;
        }

        final Window window = scene.getWindow();
        TKStage stagePeer = window.impl_getPeer();
        try {
            final Field platformWindowField = stagePeer.getClass()
                                                       .getDeclaredField("platformWindow");
            platformWindowField.setAccessible(true);
            final com.sun.glass.ui.Window platformWindow = (com.sun.glass.ui.Window) platformWindowField.get(stagePeer);
            FutureTask<Long> nativeWindowTask = new FutureTask<>(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return platformWindow.getNativeWindow();
                }
            });
            PlatformImpl.runLater(nativeWindowTask);
            return nativeWindowTask.get();
        } catch(ExecutionException | InterruptedException | NoSuchFieldException | IllegalAccessException e) {
            LOG.error("Error while trying to find native window id from JavaFX Node " + this.node,
                      e);
            return 0L;
        }
    }

    @Override
    public int hashCode() {
        return getNativeHandle().intValue();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof DisplaySurfaceHandle) {
            final DisplaySurfaceHandle otherObj = (DisplaySurfaceHandle) obj;
            return getNativeHandle().equals(otherObj.getNativeHandle());
        }

        return false;
    }
}
