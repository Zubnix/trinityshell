package org.trinity.foundation.render.javafx.api;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FXDisplaySurfaceHandleTest {

    @Mock
    private Window window;
    @Mock
    private Scene scene;

    @Mock
    private Node node;
    @InjectMocks
    private FXDisplaySurfaceHandle fxDisplaySurfaceHandle;

    @Ignore
    @Test
    public void testGetNativeHandle(){
        //FIXME this test relies on the internal implementation of javafx

        //given
        //a javafx node
        //a fx display surface handle

        //when
        //the native handle is requested

        //then
        //the handle of the native surface is returned
    }

    @Ignore
    @Test
    public void testEquals(){
        //FIXME this test relies on the internal implementation of javafx

        //given
        //2 javafx nodes with the same native surface
        //2 fx display surface handles

        //when
        //the fx display surface handles are compared for equality

        //then
        //both fx display surface handles are considered equal

    }

    @Ignore
    @Test
    public void testHashCode(){
        //FIXME this test relies on the internal implementation of javafx

        //given
        //2 javafx nodes with the same native surface
        //2 fx display surface handles

        //when
        //the fx display surface handles hash codes are compared

        //then
        //both hash codes are the same
    }
}
