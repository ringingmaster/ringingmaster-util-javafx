package org.ringingmaster.util.javafx.virtualcanvas;

import javafx.scene.canvas.GraphicsContext;

/**
 * TODO comments???
 *
 * @author Steve Lake
 */
public interface ViewportDrawer {

    void draw(GraphicsContext gc, double width, double height, double hOffset, double yOffset);
}
