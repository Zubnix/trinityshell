///*******************************************************************************
// * Trinity Shell Copyright (C) 2011 Erik De Rijcke
// *
// * This file is part of Trinity Shell.
// *
// * Trinity Shell is free software; you can redistribute it and/or modify it
// * under the terms of the GNU General Public License as published by the Free
// * Software Foundation; either version 3 of the License, or (at your option) any
// * later version.
// *
// * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
// * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// * details.
// *
// * You should have received a copy of the GNU General Public License along with
// * this program; if not, write to the Free Software Foundation, Inc., 51
// * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
// ******************************************************************************/
//
//package org.trinity.foundation.api.render.binding;
//
//import com.google.common.util.concurrent.ListeningExecutorService;
//import dagger.ObjectGraph;
//import org.trinity.foundation.api.render.binding.model.PropertyChanged;
//
//import javax.inject.Inject;
//import java.lang.annotation.Annotation;
//
//// TODO documentation
///***************************************
// * Post processes a method annotated with {@link PropertyChanged}. It reads the
// * {@link PropertyChanged#value()} and finds for each String value a
// * matching method on the view model. Each
// * matching property will be read and it's corresponding
// * method on the view model will be invoked.
// * <p>
// * This class is used by Google Guice AOP.
// ***************************************
// */
////TODO
//public class PropertyChangedSignalDispatcher {
//
//	private ViewBinder  viewBinder;
//	private ObjectGraph injector;
//
//	@Override
//	public Object invoke(final MethodInvocation invocation) throws Throwable {
//		final Object invocationResult = invocation.proceed();
//
//		final Object changedModel = invocation.getThis();
//
//		final PropertyChanged changedPropertySignal = invocation.getMethod().getAnnotation(PropertyChanged.class);
//
//		final ListeningExecutorService modelExecutorInstance = this.injector.getInstance(Key
//																								 .get(ListeningExecutorService.class,
//																									  executor));
//		final String[] changedPropertyNames = changedPropertySignal.value();
//
//		for(final String propertyName : changedPropertyNames) {
//			this.viewBinder.updateDataModelBinding(
//                                               changedModel,
//                                               propertyName);
//		}
//
//		return invocationResult;
//	}
//
//	@Inject
//	void setBinder(final ViewBinder viewBinder) {
//		this.viewBinder = viewBinder;
//	}
//
//	@Inject
//	void setInjector(final ObjectGraph injector) {
//		this.injector = injector;
//	}
//}
