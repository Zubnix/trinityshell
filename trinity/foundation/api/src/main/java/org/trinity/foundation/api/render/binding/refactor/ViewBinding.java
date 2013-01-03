package org.trinity.foundation.api.render.binding.refactor;

import java.lang.reflect.InvocationTargetException;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class ViewBinding {

	private final Binder binder;
	private final Object view;
	private final Object model;

	@AssistedInject
	ViewBinding(final Binder binder, @Assisted final Object viewModel, @Assisted final Object view) {
		this.binder = binder;

		this.model = viewModel;
		this.view = view;
	}

	public void bindSubModels() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.binder.bindSubModels(	this.model,
									this.view);
	}

	public void bindInput() {
		this.binder.bindInputs(	this.model,
								this.view);
	}

	public void bindProperty() {
		this.binder.bindProperties(	this.model,
									this.view);
	}

	public void bindCollections() {
		this.binder.bindCollections(this.model,
									this.view);
	}
}
