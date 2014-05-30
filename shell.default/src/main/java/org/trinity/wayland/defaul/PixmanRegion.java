package org.trinity.wayland.defaul;

import com.google.auto.factory.AutoFactory;
import org.pixman.LibPixman;
import org.pixman.pixman_box32;
import org.pixman.pixman_region32;
import org.trinity.shell.scene.api.Region;

import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.pixman.LibPixman.pixman_region32_rectangles;
import static org.pixman.LibPixman.pixman_region32_union_rect;

@AutoFactory
public class PixmanRegion implements Region {

    private pixman_region32 pixman_region32 = new pixman_region32();

    PixmanRegion() {
    }

    @Override
    public List<RectangleImmutable> asList() {
        //int pointer
        ByteBuffer n_rects = ByteBuffer.allocateDirect(4);
        final long boxArrayPointer = pixman_box32.getCPtr(pixman_region32_rectangles(this.pixman_region32,
                                                                                     n_rects));
        int rectangles = n_rects.getInt();

        List<RectangleImmutable> boxes = new ArrayList<>(rectangles);
        for (int i = 0; i < rectangles; i++) {
            pixman_box32 pixman_box32 = new pixman_box32(boxArrayPointer + i,
                                                         true);
            final int x = pixman_box32.getX1();
            final int y = pixman_box32.getY1();

            final int width  = pixman_box32.getX2() - x;
            final int height = pixman_box32.getY2() - y;
            boxes.add(new Rectangle(x,
                                    y,
                                    width,
                                    height));
        }
        return boxes;
    }

    @Override
    public void add(RectangleImmutable rectangle) {
        final pixman_region32 new_pixman_region32 = new pixman_region32();
        pixman_region32_union_rect(new_pixman_region32,
                                   this.pixman_region32,
                                   rectangle.getX(),
                                   rectangle.getY(),
                                   rectangle.getWidth(),
                                   rectangle.getHeight());
        this.pixman_region32 = new_pixman_region32;
    }

    @Override
    public void subtract(RectangleImmutable rectangle) {
        final pixman_region32 delta_pixman_region32 = new pixman_region32();
        LibPixman.pixman_region32_init_rect(delta_pixman_region32,
                                            rectangle.getX(),
                                            rectangle.getY(),
                                            rectangle.getWidth(),
                                            rectangle.getHeight());

        final pixman_region32 new_pixman_region32 = new pixman_region32();
        LibPixman.pixman_region32_subtract(new_pixman_region32,
                                           delta_pixman_region32,
                                           this.pixman_region32);
        this.pixman_region32 = new_pixman_region32;
    }

    public pixman_region32 getPixmanRegion32() {
        return this.pixman_region32;
    }
}
