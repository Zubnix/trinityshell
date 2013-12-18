package org.trinity.foundation.render.javafx.api;


import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;
import org.trinity.foundation.api.render.binding.ViewBinder;
import org.trinity.foundation.api.render.binding.view.SubView;

import javax.annotation.Nonnull;
import javax.inject.Inject;


public class FXView extends Control {

	@SubView
	public Skin<?> skin;

	@Inject
	protected FXView(@Nonnull final ListeningExecutorService modelExecutor,
					 @Nonnull final ViewBinder viewBinder) {
		skinProperty().addListener(new ChangeListener<Skin<?>>() {
			@Override
			public void changed(final ObservableValue<? extends Skin<?>> observableValue,
								final Skin<?> oldSkin,
								final Skin<?> newSkin) {
				FXView.this.skin = newSkin;
				viewBinder.updateViewModelBinding(modelExecutor,
												  FXView.this,
												  "skin",
												  Optional.fromNullable(oldSkin),
												  Optional.fromNullable(newSkin));
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