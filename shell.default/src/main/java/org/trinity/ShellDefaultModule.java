package org.trinity;

import dagger.Module;
import dagger.Provides;
import jnr.ffi.LibraryLoader;

@Module
public class ShellDefaultModule {

    @Provides
    LibC provideLibC(){
        return LibraryLoader.create(LibC.class)
                            .load("c");
    }
}
