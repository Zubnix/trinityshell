package org.trinity.shell.scene.api;

/**
 *
 */
public interface ShellNodeConfigurable {

    void configure(int x, int y, int width, int height);

    void setParent(ShellNodeParent shellNodeParent);

    void markDestroyed();

    void setVisible(Boolean visible);
}
