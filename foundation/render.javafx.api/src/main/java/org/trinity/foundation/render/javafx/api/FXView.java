package org.trinity.foundation.render.javafx.api;


import com.cathive.fx.guice.GuiceFXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;


public class FXView extends Region {

    private static final Logger LOG = LoggerFactory.getLogger(FXView.class);

    private final String resourcePath = "%s.fxml";
    private final GuiceFXMLLoader loader;

    protected FXView(@Nonnull final GuiceFXMLLoader loader) {
        this.loader = loader;
        this.loadFXML();
    }

    private void loadFXML() {
        try {
            final GuiceFXMLLoader.Result result = loader.load(this.getViewURL());
            final Node root = result.getRoot();
            this.getChildren().add(root);
        } catch (IOException ex) {
            LOG.error("",
                    ex);
        }
    }

    private String getViewPath() {
        return String.format(resourcePath,
                this.getClass().getSimpleName());
    }

    private URL getViewURL() {
        return this.getClass().getClassLoader().getResource(this.getViewPath());
    }

    public void setParent(final FXView parentFxView) {
        parentFxView.getChildren().add(this);
    }

    public void close() {
        final FXView parent = (FXView) getParent();
        parent.getChildren().remove(this);
    }
}