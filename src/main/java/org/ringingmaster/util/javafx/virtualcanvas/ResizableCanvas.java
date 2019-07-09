package org.ringingmaster.util.javafx.virtualcanvas;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO comments???
 *
 * @author Steve Lake
 */
public class ResizableCanvas extends Canvas {

    private final Logger log = LoggerFactory.getLogger(ResizableCanvas.class);

    double hOrigin;
    double vOrigin;
    private ViewportDrawer viewportDrawer = (gc, viewport) -> {};

    public  ResizableCanvas() {
        super(0, 0);

        widthProperty().addListener(it -> render());
        heightProperty().addListener(it -> render());
    }


    public void setViewportDrawer(ViewportDrawer viewportDrawer) {
        this.viewportDrawer = checkNotNull(viewportDrawer);
    }

    @Override
    public double prefHeight(double width) {
        return 0;
    }


    @Override
    public double prefWidth(double height) {
        return 0;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    public void setOrigin(double hOrigin, double vOrigin) {
        this.hOrigin = hOrigin;
        this.vOrigin = vOrigin;

        render();
    }

    protected void render() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.save();
        gc.translate(-hOrigin,-vOrigin);
        viewportDrawer.draw(gc, new BoundingBox(hOrigin, vOrigin, getWidth(), getHeight()));

        gc.restore();
    }
}
