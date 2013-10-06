package org.trinity.foundation.render.javafx.api;


import javafx.scene.control.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FXView extends Control {

    private static final Logger LOG = LoggerFactory.getLogger(FXView.class);


    public void setParent(final FXView parentFxView) {
        parentFxView.getChildren().add(this);
    }

    public void close() {
        final FXView parent = (FXView) getParent();
        parent.getChildren().remove(this);
    }
}