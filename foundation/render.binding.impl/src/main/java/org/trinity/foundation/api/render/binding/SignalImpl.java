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

package org.trinity.foundation.api.render.binding;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.hash.HashFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

//TODO documentation
public class SignalImpl implements Signal {

    private static final Logger LOG = LoggerFactory.getLogger(SignalImpl.class);
    private static final Cache<HashCode, Optional<Method>> EVENT_SLOTS_BY_HASH = CacheBuilder.newBuilder().concurrencyLevel(1)
            .build();
    private static final HashFunction HASH_FUNCTION = Hashing.goodFastHash(16);
    private final ListeningExecutorService modelExecutor;
    private final Object view;
    private final Map<Object, Object> dataContextValueByView;
    private final String inputSlotName;

    SignalImpl(final ListeningExecutorService modelExecutor,
               final Object view,
               final Map<Object, Object> dataContextValueByView,
               final String inputSlotName) {
        this.modelExecutor = modelExecutor;
        this.view = view;
        this.dataContextValueByView = dataContextValueByView;
        this.inputSlotName = inputSlotName;
    }

    private static Optional<Method> findSlot(final Class<?> modelClass,
                                             final String methodName) throws ExecutionException {

        final HashCode hashCode = HASH_FUNCTION.newHasher().putInt(modelClass.hashCode())
                .putUnencodedChars(methodName).hash();

        return EVENT_SLOTS_BY_HASH.get(hashCode,
                new Callable<Optional<Method>>() {
                    @Override
                    public Optional<Method> call() {
                        return getSlot(modelClass,
                                methodName);
                    }
                });
    }

    private static Optional<Method> getSlot(final Class<?> modelClass,
                                            final String methodName) {
        Method inputSlot = null;
        try {
            inputSlot = modelClass.getMethod(methodName);
        } catch (final SecurityException e) {
            LOG.error("Error while trying to find an input slot for class=" + modelClass + " with slotname="
                    + methodName,
                    e);
        } catch (final NoSuchMethodException e) {
            LOG.warn("No input slot found for class=" + modelClass + " with slotname=" + methodName,
                    e);
        }
        return Optional.fromNullable(inputSlot);
    }

    @Override
    public ListenableFuture<Void> fire() {
        return this.modelExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final Object viewModel = SignalImpl.this.dataContextValueByView.get(SignalImpl.this.view);
                final Optional<Method> optionalInputSlot = findSlot(viewModel.getClass(),
                                                                    SignalImpl.this.inputSlotName);
                if(optionalInputSlot.isPresent()) {
                    final Method method = optionalInputSlot.get();
                    method.setAccessible(true);
                    method.invoke(viewModel);
                }
                return null;
            }
        });
    }
}
