package org.trinity;

import dagger.Module;

@Module(
        injects = {
                PixmanRegionFactory.class,
                SimpleShellSurfaceFactory.class
        }
)
public class ShellDefaultModule {


}
