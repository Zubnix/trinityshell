package org.trinity.foundation.render.javafx.api;


import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FXView extends Control {

    private static final Logger LOG = LoggerFactory.getLogger(FXView.class);


    public void setParent(final Pane parentFxView) {
        parentFxView.getChildren().add(this);
        parentFxView.layout();
    }

    public void close() {
        final Pane parent = (Pane) getParent();
        parent.getChildren().remove(this);
    }
}