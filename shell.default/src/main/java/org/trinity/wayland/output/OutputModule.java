package org.trinity.wayland.output;

import com.google.common.util.concurrent.Service;
import dagger.Module;
import dagger.Provides;
import org.freedesktop.wayland.server.Display;
import org.trinity.CLibrary;

import javax.inject.Singleton;

import static dagger.Provides.Type.SET;

@Module(injects = {
        ShmRendererFactory.class,
        CompositorFactory.class
},
        library = true,
        //needs render engine implementation, defined at startup.
        complete = false
)
public class OutputModule {


    @Provides
    @Singleton
    Display provideDisplay() {
        return Display.create();
    }

    @Provides
    @Singleton
    Scene provideWlScene() {
        return new Scene();
    }


    @Singleton
    @Provides
    JobExecutor provideWlJobExecutor(final Display display) {
        final int[] pipe = configure(pipe());
        int pipeR = pipe[0];
        int pipeWR = pipe[1];

        return new JobExecutor(display,
                               pipeR,
                               pipeWR);
    }

    private int[] pipe() {
        final int[] pipeFds = new int[2];
        CLibrary.INSTANCE.pipe(pipeFds);
        return pipeFds;
    }

    private int[] configure(final int[] pipeFds) {
        final int readFd = pipeFds[0];
        final int writeFd = pipeFds[1];

        final int readFlags = CLibrary.INSTANCE.fcntl(readFd,
                                                      CLibrary.F_GETFD,
                                                      0);
        CLibrary.INSTANCE.fcntl(readFd,
                                CLibrary.F_SETFD,
                                readFlags | CLibrary.FD_CLOEXEC);

        final int writeFlags = CLibrary.INSTANCE.fcntl(writeFd,
                                                       CLibrary.F_GETFD,
                                                       0);
        CLibrary.INSTANCE.fcntl(writeFd,
                                CLibrary.F_SETFD,
                                writeFlags | CLibrary.FD_CLOEXEC);

        return pipeFds;
    }

    @Provides(type = SET)
    Service provideService(final ShellService shellService) {
        return shellService;
    }
}
