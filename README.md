
[![Build Status](https://travis-ci.org/Zubnix/trinityshell.png?branch=0.0.3-SNAPSHOT)](https://travis-ci.org/Zubnix/trinityshell)

Trinity shell is a Java base desktop shell for Linux.  
It is higly modular by design and uses Google Guice to glue different functional modules together. Currently the display module uses XCB through XCB4J to talk to the X display server while the render module uses Qt through QtJambi to do the drawing. Window manager and shell functionality is provided by shell plugins that are loaded by the shell module.  
It is still a young project that is usable but not usefull as a lot of very basic window managing controls are still missing. The project's ambition is to become a tool and library for Java developers to build their own fully fledged desktop, including hardware accelerated compositing, tiling window management etc.

#Building  and Running
Checkout and build Xcb4j. Xcb4j can be found on Github.
Checkout and build Trinity Shell master branch. Go into the trinity directory and type "mvn clean install -DskipTests". Next we will build a jar with all dependencies included. Go in the bootstrap directory and type "mvn assembly:single". This will create a 'fat' jar in the target directory. Startup your favority X test server (like Xephyr) eg "Xephyr -ac :1". Set the DISPLAY environment variable to :1 with the command "export DISPLAY=:1". Now you can start trinity shell "java -jar bootstrap-0.0.1-SNAPSHOT-jar-with-dependencies.jar". You'll still have to start programs from outside Trinity Shell, in command line type "xterm -display :1" and you should see them tiled up next to eachother. That's it! Cheers! If you encounter any problems you can post them in trinity shell or xcb4j respectively..
