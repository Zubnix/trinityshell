package org.trinity.shell.plugin.impl;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.shell.api.plugin.ShellPlugin;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ShellPluginsRunnerImplTest {

    @Mock
    private ShellPlugin shellPlugin0;
    @Mock
    private ShellPlugin shellPlugin1;
    @Mock
    private ShellPlugin shellPlugin2;
    @Mock
    private ShellPlugin shellPlugin3;

    private ShellPluginsRunnerImpl shellPluginsRunner;

    @Before
    public void setUp() {
        final Set<ShellPlugin> shellPlugins = new HashSet<ShellPlugin>() {{
            add(shellPlugin0);
            add(shellPlugin1);
            add(shellPlugin2);
            add(shellPlugin3);
        }};

        shellPluginsRunner = new ShellPluginsRunnerImpl(shellPlugins);
    }

    @Test
    public void testStart() {
        //given
        //shell plugins
        //a shell plugins runner

        //when
        //the shell plugins runner is started
        shellPluginsRunner.startAll();

        //then
        //each shell plugin is started
        verify(shellPlugin0).startAsync();
        verify(shellPlugin1).startAsync();
        verify(shellPlugin2).startAsync();
        verify(shellPlugin3).startAsync();
    }

    @Test
    public void testStop() {
        //given
        //shell plugins
        //a shell plugins runner

        //when
        //the shell plugins runner is stopped
        shellPluginsRunner.stopAll();

        //then
        //each shell plugin is stopped
        verify(shellPlugin0).stopAsync();
        verify(shellPlugin1).stopAsync();
        verify(shellPlugin2).stopAsync();
        verify(shellPlugin3).stopAsync();
    }
}
