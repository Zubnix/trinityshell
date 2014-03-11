package org.trinity.shell.scene.api;

/**
 *
 */
public interface ShellNodeConfiguration<T extends ShellNodeConfigurable> {
    void visit(T shellNodeConfigurable);
}
