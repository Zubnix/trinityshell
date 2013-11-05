package org.trinity.foundation.render.javafx.api;


import com.google.common.util.concurrent.ListeningExecutorService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.render.binding.view.SubView;

import javax.annotation.Nonnull;
import javax.inject.Inject;


public class FXView extends Control {

    private static final Logger LOG = LoggerFactory.getLogger(FXView.class);

    @SubView
    public Skin<?> skin;

    @Inject
    protected FXView(@Nonnull final ListeningExecutorService modelExecutor,
                     @Nonnull final Binder binder) {
        skinProperty().addListener(new ChangeListener<Skin<?>>() {
            @Override
            public void changed(final ObservableValue<? extends Skin<?>> observableValue,
                                final Skin<?> oldSkin,
                                final Skin<?> newSkin) {
                FXView.this.skin = newSkin;
                binder.updateViewModelBinding(modelExecutor,
                                              FXView.this,
                                              "skin");
            }
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