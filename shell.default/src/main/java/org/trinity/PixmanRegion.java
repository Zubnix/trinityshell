package org.trinity;

import com.google.auto.factory.AutoFactory;
import org.freedesktop.pixman1.Pixman1Library;
import org.freedesktop.pixman1.pixman_box32;
import org.freedesktop.pixman1.pixman_region32;
import org.trinity.shell.scene.api.Region;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

@AutoFactory(className = "PixmanRegionFactory")
public class PixmanRegion implements Region {

    private pixman_region32 pixman_region32 = new pixman_region32();

    PixmanRegion() {
    }

    @Override
    public List<RectangleImmutable> asList() {
        //int pointer
        final IntBuffer n_rects = ByteBuffer.allocateDirect(4).asIntBuffer();
        final pixman_box32 pixman_box32_array = Pixman1Library.INSTANCE.pixman_region32_rectangles(this.pixman_region32,
                                                                                              n_rects);
        final int size = n_rects.get();
        final pixman_box32[] pixman_box32s = (pixman_box32[]) pixman_box32_array.toArray(size);

        final List<RectangleImmutable> boxes = new ArrayList<>(size);
        for (final pixman_box32 pixman_box32:pixman_box32s) {
            final int x = pixman_box32.x1;
            final int y = pixman_box32.y1;

            final int width  = pixman_box32.x2 - x;
            final int height = pixman_box32.y2 - y;
            boxes.add(new Rectangle(x,
                                    y,
                                    width,
                                    height));
        }
        return boxes;
    }

    @Override
    public Region add(@Nonnull final RectangleImmutable rectangle) {
        final pixman_region32 new_pixman_region32 = new pixman_region32();
        //FIXME check result.

        final int result = Pixman1Library.INSTANCE.pixman_region32_union_rect(new_pixman_region32,
                                                                              this.pixman_region32,
                                                                              rectangle.getX(),
                                                                              rectangle.getY(),
                                                                              rectangle.getWidth(),
                                                                              rectangle.getHeight());
        this.pixman_region32 = new_pixman_region32;

        return this;
    }

    @Override
    public Region subtract(@Nonnull final RectangleImmutable rectangle) {
        final pixman_region32 delta_pixman_region32 = new pixman_region32();
        Pixman1Library.INSTANCE.pixman_region32_init_rect(delta_pixman_region32,
                                                          rectangle.getX(),
                                                          rectangle.getY(),
                                                          rectangle.getWidth(),
                                                          rectangle.getHeight());

        final pixman_region32 new_pixman_region32 = new pixman_region32();
        Pixman1Library.INSTANCE.pixman_region32_subtract(new_pixman_region32,
                                           delta_pixman_region32,
                                           this.pixman_region32);
        this.pixman_region32 = new_pixman_region32;

        return this;
    }

    public pixman_region32 getPixmanRegion32() {
        return this.pixman_region32;
    }
}
