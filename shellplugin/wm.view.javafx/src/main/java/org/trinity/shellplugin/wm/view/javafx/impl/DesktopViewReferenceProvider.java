package org.trinity.shellplugin.wm.view.javafx.impl;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;

import javax.annotation.Nonnull;
import java.io.IOException;


public class DesktopViewReferenceProvider extends AbstractFXViewReferenceProvider {

    private GuiceFXMLLoader fxmlLoader;

    @Inject
    DesktopViewReferenceProvider(
            @Nonnull @DisplayExecutor ListeningExecutorService displayExecutor,
            @Nonnull DisplaySurfacePool displaySurfacePool,
            GuiceFXMLLoader fxmlLoader
    ) {
        super(
                displayExecutor,
                displaySurfacePool,
                fxmlLoader
        );
        this.fxmlLoader = fxmlLoader;
    }

    @Override
    protected GuiceFXMLLoader.Result createViewObject() {
        try {
            return this.fxmlLoader.load(getClass().getResource("Desktop.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
