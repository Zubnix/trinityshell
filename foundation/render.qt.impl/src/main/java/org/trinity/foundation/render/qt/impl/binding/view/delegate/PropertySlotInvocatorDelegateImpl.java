/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QApplication;

@Bind
@Singleton
public class PropertySlotInvocatorDelegateImpl implements PropertySlotInvocatorDelegate {

	PropertySlotInvocatorDelegateImpl() {
	}

	@Override
	public ListenableFuture<Void> invoke(	final Object view,
											final Method viewMethod,
											final Object argument) {
		final ListenableFutureTask<Void> invokeTask = ListenableFutureTask.create(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				viewMethod.invoke(	view,
									argument);
				return null;
			}
		});

		QApplication.invokeLater(invokeTask);
		return invokeTask;
	}
}
