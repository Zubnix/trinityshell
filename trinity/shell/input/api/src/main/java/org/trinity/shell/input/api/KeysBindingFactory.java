package org.trinity.shell.input.api;

import java.util.List;

import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;

public interface KeysBindingFactory {
	KeysBinding createKeysBinding(	List<Key> keys,
									InputModifiers inputModifiers,
									Runnable action);
}
