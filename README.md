
[![Build Status](https://travis-ci.org/Zubnix/trinityshell.png?branch=0.0.3-SNAPSHOT)](https://travis-ci.org/Zubnix/trinityshell)

Trinity shell is a Java base desktop shell for Linux.  
It is higly modular by design and uses Google Guice to glue different functional modules together. Currently the display module uses XCB through XCB4J to talk to the X display server while the render module uses Qt through QtJambi to do the drawing. Window manager and shell functionality is provided by shell plugins that are loaded by the shell module.  
It is still a young project that is usable but not usefull as a lot of very basic window managing controls are still missing. The project's ambition is to become a tool and library for Java developers to build their own fully fledged desktop, including hardware accelerated compositing, tiling window management etc.

##Building  and Running

- `git clone git@github.com:Zubnix/trinityshell.git` Clone the project
- `git submodule init` Initialie the submodules
- `git submodule update` Update the submodules
- `cd externals/xcb4j` Go to the xcb4j submodule
- `mvn install` Build the xcb4j sudmoule. In case xcb4j doesn't want to build, have a look at the xcb4j project on github.
- `cd ../..` Go back to the project directory
- `mvn install` Build the project
- `cd bootstrap` Go to the bootstrap directory
- `mvn assembly:assembly` Build a fat jar of the project

Now startup your favority X test server (like Xephyr) eg `Xephyr -ac :1`. Set the DISPLAY environment variable to :1 with the command `export DISPLAY=:1`. Now you can start trinity shell java -jar bootstrap-0.0.2-SNAPSHOT-jar-with-dependencies.jar`. If you see errors related to libpng, install libpng12 using your distro's package manager.

You'll still have to start programs from outside Trinity Shell, in command line type "xterm -display :1" and you should see them tiled up next to eachother. That's it! Cheers! If you encounter any problems you can post them in trinity shell or xcb4j respectively.

##Branches

- master: Uses QtJambi (Qt bindings for java) and X. JDK7.
- 0.0.3-SNAPSHOT: Uses JavaFX and X. Based on master. JDK8.
- 0.1.0-SNAPSHOT: Targets wayland, the successor of X. Complete rewrite. JDK8.
