SHELL = /bin/sh
CC = g++
TARGETDIR = ../../resources/META-INF/lib
LIBNAME = libfusionqt.so

vpath %.c $(LIBSRCDIR)
JAVA_INCLUDE_DIR = $(JAVA_HOME)/include
JNI = $(JAVA_INCLUDE_DIR)
JNI_LINUX = $(JAVA_INCLUDE_DIR)/linux

#Change this to whatever dir the qt and qtjambi libs are located in.
QT_LIBDIR = /usr/lib/qt4
QT_JAMBI_LIBDIR = /usr/lib/qtjambi-4

QT_INC_DIR = /usr/include/qt4

CFLAGS = -march=native -O2 -fPIC -c -g -Wall -I$(JNI) -I$(JNI_LINUX) -I$(QT_INC_DIR) -fno-stack-protector
LFLAGS = -L$(QT_LIBDIR) -L$(QT_JAMBI_LIBDIR) -lQtCore -lqtjambi -lQtGui
objects = fusionqt.o

all : $(objects)
	$(CC) -shared -Wl,-soname,$(LIBNAME) -o $(TARGETDIR)/$(LIBNAME) $(LFLAGS) $(objects)

fusionqt.o : fusionqt.cpp
	$(CC) $(CFLAGS) $< -o $@