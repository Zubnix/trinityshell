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

package org.trinity.foundation.api.binding.binding;

import com.google.auto.factory.AutoFactory;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.binding.api.view.delegate.Signal;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoFactory(
        className = "SignalFactory"
)
public class SignalDefault implements Signal {

	private static final Logger                          LOG                 = LoggerFactory.getLogger(SignalDefault.class);
	private static final Map<HashCode, Optional<Method>> EVENT_SLOTS_BY_HASH = new HashMap<>();
	private static final HashFunction                    HASH_FUNCTION       = Hashing.goodFastHash(16);

	private final Object                   eventSignalReceiver;
	private final Optional<Method>         slot;

	SignalDefault(@Nonnull final Object eventSignalReceiver,
                  @Nonnull final String inputSlotName) {
        checkNotNull(eventSignalReceiver);
        checkNotNull(inputSlotName);

		this.eventSignalReceiver = eventSignalReceiver;
		this.slot = findSlot(this.eventSignalReceiver.getClass(),
							 inputSlotName);
	}

    @Override
    public boolean equals(final Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }

        final SignalDefault other = (SignalDefault) obj;

        return Objects.equal(   this.eventSignalReceiver,
                                other.eventSignalReceiver) && Objects.equal(this.slot,
                                                                            other.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.eventSignalReceiver,
                                this.slot);
    }

    private static Optional<Method> findSlot(final Class<?> modelClass,
											 final String methodName) {

		final HashCode hashCode = HASH_FUNCTION.newHasher().putInt(modelClass.hashCode())
											   .putUnencodedChars(methodName).hash();

		Optional<Method> methodOptional = EVENT_SLOTS_BY_HASH.get(hashCode);
		if(methodOptional == null) {
			methodOptional = getSlot(modelClass,
									 methodName);
			EVENT_SLOTS_BY_HASH.put(hashCode,
									methodOptional);
		}

		return methodOptional;
	}

	private static Optional<Method> getSlot(final Class<?> modelClass,
											final String methodName) {
		Method inputSlot = null;
		try {
			inputSlot = modelClass.getMethod(methodName);
		}
		catch(final SecurityException e) {
			LOG.error("Error while trying to find an input slot for class=" + modelClass + " with slotname="
							  + methodName,
					  e);
		}
		catch(final NoSuchMethodException e) {
			LOG.warn("No event slot found for class=" + modelClass + " with slotname=" + methodName,
					 e);
		}
		return Optional.fromNullable(inputSlot);
	}

	@Override
	public void fire() {
		if(SignalDefault.this.slot.isPresent()) {
			final Method method = SignalDefault.this.slot.get();
			method.setAccessible(true);
			try {
				method.invoke(SignalDefault.this.eventSignalReceiver);
			}
			catch(final IllegalAccessException e) {
				e.printStackTrace();
			}
			catch(final InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
