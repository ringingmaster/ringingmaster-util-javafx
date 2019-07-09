package org.ringingmaster.util.javafx.virtualcanvas;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;

/**
 * TODO comments???
 *
 * @author Steve Lake
 */
public interface ViewportDrawer {

    void draw(GraphicsContext gc, Bounds viewport);
}
