package org.trinity.foundation.api.render.binding;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.binding.view.InputSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.SubModel;
import org.trinity.foundation.api.render.binding.view.ViewElementTypes;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.inject.Inject;

public class NewBinder {

	private static final String GET_BOOLEAN_PREFIX = "is";
	private static final String GET_PREFIX = "get";

	private final BindingAnnotationScanner bindingAnnotationScanner;
	private final ViewElementTypes viewElementTypes;

	@Inject
	NewBinder(final BindingAnnotationScanner bindingAnnotationScanner, final ViewElementTypes viewElementTypes) {
		this.bindingAnnotationScanner = bindingAnnotationScanner;
		this.viewElementTypes = viewElementTypes;
	}

	public void bind(	final Object model,
						final Object view) throws ExecutionException {

		bindViewElement(model,
						view,
						Optional.<SubModel> absent(),
						Optional.<InputSignals> absent(),
						Optional.<ObservableCollection> absent(),
						Optional.<PropertySlots> absent());
	}

	public void updateView(	final Object model,
							final String propertyName,
							final Object propertyValue) {
		// TODO find bound views & call property slots

		// TODO find out if any views with submodels had this property in their
		// chain
		// & rebind them if so.
	}

	protected void bindViewElement(	final Object inheritedModel,
									final Object view,
									final Optional<SubModel> optionalFieldSubModel,
									final Optional<InputSignals> optionalFieldInputSignals,
									final Optional<ObservableCollection> optionalFieldObservableCollection,
									final Optional<PropertySlots> optionalFieldPropertySlots) throws ExecutionException {
		checkNotNull(inheritedModel);
		checkNotNull(view);

		final Class<?> viewClass = view.getClass();

		// check for classlevel annotations if field level annotations are
		// absent
		final Optional<SubModel> optionalSubModel = optionalFieldSubModel.or(Optional.<SubModel> fromNullable(viewClass
				.getAnnotation(SubModel.class)));
		final Optional<InputSignals> optionalInputSignals = optionalFieldInputSignals.or(Optional
				.<InputSignals> fromNullable(viewClass.getAnnotation(InputSignals.class)));
		final Optional<ObservableCollection> optionalObservableCollection = optionalFieldObservableCollection
				.or(Optional.<ObservableCollection> fromNullable(viewClass.getAnnotation(ObservableCollection.class)));
		final Optional<PropertySlots> optionalPropertySlots = optionalFieldPropertySlots.or(Optional
				.<PropertySlots> fromNullable(viewClass.getAnnotation(PropertySlots.class)));

		Object model = inheritedModel;
		if (optionalSubModel.isPresent()) {
			final String propertyChain = optionalSubModel.get().value();
			final Optional<Object> optionalSubModelInstance = getSubModelInstance(	model,
																					propertyChain);
			if (optionalSubModelInstance.isPresent()) {
				model = optionalSubModelInstance.get();
			} else {
				return;
			}
		}

		if (optionalInputSignals.isPresent()) {
			final InputSignal[] inputSignals = optionalInputSignals.get().value();
			// TODO install input emitters
		}

		if (optionalObservableCollection.isPresent()) {
			final ObservableCollection observableCollection = optionalObservableCollection.get();
			// TODO link to observableCollection
		}

		if (optionalPropertySlots.isPresent()) {
			final PropertySlots propertySlots = optionalPropertySlots.get();
			// TODO link to propertySlots
		}

		bindChildViewElements(	model,
								view);
	}

	protected void bindChildViewElements(	final Object inheritedModel,
											final Object view) throws ExecutionException {
		checkNotNull(inheritedModel);
		checkNotNull(view);

		final Class<?> viewClass = view.getClass();

		final Field[] childViewElements = viewClass.getDeclaredFields();

		for (final Field childViewElement : childViewElements) {

			childViewElement.setAccessible(true);
			Object childView;
			try {
				childView = childViewElement.get(view);
			} catch (final IllegalArgumentException e) {
				throw new ExecutionException(e);
			} catch (final IllegalAccessException e) {
				throw new ExecutionException(e);
			}
			childViewElement.setAccessible(false);

			// filter out null values
			if (childView == null) {
				continue;
			}

			// filter out types we're not interested in
			boolean interestedViewElement = false;
			for (final Class<?> viewElementType : this.viewElementTypes.getViewElementTypes()) {
				interestedViewElement |= viewElementType.isAssignableFrom(childView.getClass());
			}
			if (!interestedViewElement) {
				continue;
			}

			final Optional<SubModel> optionalFieldSubModel = Optional.<SubModel> fromNullable(childViewElement
					.getAnnotation(SubModel.class));
			final Optional<InputSignals> optionalFieldInputSignals = Optional
					.<InputSignals> fromNullable(childViewElement.getAnnotation(InputSignals.class));
			final Optional<ObservableCollection> optionalFieldObservableCollection = Optional
					.<ObservableCollection> fromNullable(childViewElement.getAnnotation(ObservableCollection.class));
			final Optional<PropertySlots> optionalFieldPropertySlots = Optional
					.<PropertySlots> fromNullable(childViewElement.getAnnotation(PropertySlots.class));

			bindViewElement(inheritedModel,
							childView,
							optionalFieldSubModel,
							optionalFieldInputSignals,
							optionalFieldObservableCollection,
							optionalFieldPropertySlots);
		}
	}

	protected Optional<Object> getSubModelInstance(	final Object model,
													final String subModelPath) throws ExecutionException {
		checkNotNull(model);

		final Iterable<String> propertyNames = Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
		Object currentModel = model;
		for (final String propertyName : propertyNames) {
			if (currentModel == null) {
				break;
			}
			final Class<?> currentModelClass = currentModel.getClass();
			final Method foundMethod = findGetter(	currentModelClass,
													propertyName);
			try {
				currentModel = foundMethod.invoke(currentModel);
			} catch (final IllegalAccessException e) {
				throw new ExecutionException(	String.format(	"Can not access getter on %s. Is it a no argument public method?",
																currentModel),
												e);
			} catch (final IllegalArgumentException e) {
				throw new ExecutionException(e);
			} catch (final InvocationTargetException e) {
				throw new ExecutionException(e);
			}
		}

		final Iterator<String> propertyNamesIt = propertyNames.iterator();
		if (propertyNamesIt.hasNext()) {
			final String firstPropertyName = propertyNamesIt.next();
		}

		return Optional.fromNullable(currentModel);
	}

	protected Method findGetter(final Class<?> modelClass,
								final String propertyName) throws ExecutionException {
		Method foundMethod;
		String getterMethodName = toGetterMethodName(propertyName);
		try {
			foundMethod = this.bindingAnnotationScanner.lookupModelPropety(	modelClass,
																			getterMethodName);
		} catch (final ExecutionException e) {
			// no getter with get found, try with is. If nothing found, let
			// the exception bubble up.
			getterMethodName = toBooleanGetterMethodName(propertyName);
			foundMethod = this.bindingAnnotationScanner.lookupModelPropety(	modelClass,
																			getterMethodName);
		}
		return foundMethod;
	}

	protected String toGetterMethodName(final String propertyName) {
		return toGetterMethodName(	GET_PREFIX,
									propertyName);
	}

	protected String toGetterMethodName(final String prefix,
										final String propertyName) {
		return prefix + CaseFormat.LOWER_CAMEL.to(	CaseFormat.UPPER_CAMEL,
													propertyName);
	}

	protected String toBooleanGetterMethodName(final String propertyName) {
		return toGetterMethodName(	GET_BOOLEAN_PREFIX,
									propertyName);
	}
}
