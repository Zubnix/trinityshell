package org.trinity.foundation.render.javafx.api;


import com.google.common.util.concurrent.ListeningExecutorService;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.render.binding.view.SubView;

import javax.annotation.Nonnull;
import javax.inject.Inject;


public class FXView extends Control {

    @SubView
    public Skin<?> skin;

    @Inject
    protected FXView(@Nonnull final ListeningExecutorService modelExecutor,
                     @Nonnull final Binder binder) {
        skinProperty().addListener((observableValue,
                                    oldSkin,
                                    newSkin) -> {
            this.skin = newSkin;
            binder.updateViewModelBinding(modelExecutor,
                                          FXView.this,
                                          "skin");
        });
    }

    public void setParent(final Pane parentFxView) {
        parentFxView.getChildren().add(this);
        parentFxView.layout();
    }

    public void close() {
        final Pane parent = (Pane) getParent();
        parent.getChildren().remove(this);
    }
}